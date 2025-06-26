// MultiplayerClient.java
package com.example.demo;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class MultiplayerClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Thread listenerThread;

    public MultiplayerClient(String host, int port, String password, String playerName, Consumer<String> onMessageReceived) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        listenerThread = new Thread(() -> {
            try {
                String line;
                boolean sentPassword = false;
                boolean sentName = false;

                while ((line = in.readLine()) != null) {
                    onMessageReceived.accept(line);

                    if (!sentPassword && line.equals("ENTER_PASSWORD")) {
                        out.println(password);  // send password only
                        sentPassword = true;
                    } else if (sentPassword && !sentName && line.equals("PASSWORD_ACCEPTED")) {
                        out.println(playerName);  // send player name after acceptance
                        sentName = true;
                    }
                }
            } catch (IOException e) {
                onMessageReceived.accept("DISCONNECTED");
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }


    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

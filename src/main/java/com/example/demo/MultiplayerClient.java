package com.example.demo;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class MultiplayerClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Thread listenerThread;
    private Consumer<String> messageListener;

    public MultiplayerClient(String host, int port, String password, String playerName, Consumer<String> initialListener) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.messageListener = initialListener;

        listenerThread = new Thread(() -> {
            try {
                String line;
                boolean sentPassword = false;
                boolean sentName = false;

                while ((line = in.readLine()) != null) {
                    if (messageListener != null) {
                        messageListener.accept(line);
                    }

                    if (!sentPassword && line.equals("ENTER_PASSWORD")) {
                        out.println(password);
                        sentPassword = true;
                    } else if (sentPassword && !sentName && line.equals("PASSWORD_ACCEPTED")) {
                        out.println(playerName);
                        sentName = true;
                    }
                }
            } catch (IOException e) {
                if (messageListener != null) {
                    messageListener.accept("DISCONNECTED");
                }
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void setMessageListener(Consumer<String> newListener) {
        this.messageListener = newListener;
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

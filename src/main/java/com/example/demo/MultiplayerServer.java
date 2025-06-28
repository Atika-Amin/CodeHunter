package com.example.demo;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class MultiplayerServer {

    private static final int PORT = 5050;
    private static final String SERVER_PASSWORD = "code123";
    private static final int MAX_PLAYERS = 5;

    private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        String localIP = getLocalIPAddress();
        if (localIP == null) {
            System.out.println("⚠️ Unable to detect local IP address. Binding to all interfaces.");
        } else {
            System.out.println("🌐 Multiplayer Server is running on IP: " + localIP + ", Port: " + PORT);
        }

        try (ServerSocket serverSocket = (localIP != null)
                ? new ServerSocket(PORT, 50, InetAddress.getByName(localIP))
                : new ServerSocket(PORT)) { // fallback to all interfaces if IP not found

            while (true) {
                Socket clientSocket = serverSocket.accept();

                if (clients.size() >= MAX_PLAYERS) {
                    PrintWriter tempOut = new PrintWriter(clientSocket.getOutputStream(), true);
                    tempOut.println("SERVER_FULL");
                    clientSocket.close();
                    continue;
                }

                ClientHandler handler = new ClientHandler(clientSocket);
                clients.add(handler);
                handler.start();
            }
        } catch (IOException e) {
            System.out.println("❌ Server Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getLocalIPAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (ni.isUp() && !ni.isLoopback() && !ni.isVirtual()) {
                    Enumeration<InetAddress> addresses = ni.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress addr = addresses.nextElement();
                        if (addr instanceof Inet4Address) {
                            String ip = addr.getHostAddress();
                            // Check if IP is private/local network IP
                            if (ip.startsWith("192.") || ip.startsWith("10.") || ip.startsWith("172.")) {
                                return ip;  // Return the first private IPv4 address found
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("⚠️ Error detecting local IP: " + e.getMessage());
        }
        return null;
    }

    static class ClientHandler extends Thread {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String playerName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("ENTER_PASSWORD");
                String password = in.readLine();

                if (!SERVER_PASSWORD.equals(password)) {
                    out.println("INVALID_PASSWORD");
                    closeConnection();
                    return;
                }

                out.println("PASSWORD_ACCEPTED");
                playerName = in.readLine();
                System.out.println("✅ " + playerName + " joined from " + socket.getInetAddress());

                broadcastToAll("JOINED:" + playerName);
                broadcastToAll("PLAYER_COUNT:" + clients.size());

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("POS:")) {
                        // Example: POS:150,200 → POS_UPDATE:Hamim07:150,200
                        broadcastToAll("POS_UPDATE:" + playerName + ":" + message.substring(4));
                    } else {
                        System.out.println("📨 [" + playerName + "]: " + message);
                        if (message.startsWith("CHAT:")) {
                            broadcastToAll("CHAT:" + playerName + ": " + message.substring(5));
                        } else {
                            broadcastToAll(playerName + ": " + message);
                        }


                    }
                }

            } catch (IOException e) {
                System.out.println("❌ Connection with " + playerName + " lost: " + e.getMessage());
            } finally {
                clients.remove(this);
                if (playerName != null) {
                    broadcastToAll("LEFT:" + playerName);
                    broadcastToAll("PLAYER_COUNT:" + clients.size());
                }
                closeConnection();
            }
        }

        private void broadcastToAll(String message) {
            for (ClientHandler client : clients) {
                client.out.println(message);
            }
        }

        private void closeConnection() {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                System.out.println("⚠️ Error closing connection: " + e.getMessage());
            }
        }
    }
}

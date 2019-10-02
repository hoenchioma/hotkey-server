package com.rfw.hotkey_server.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private ServerSocket serverSocket;
    public volatile boolean stop = false;

    private Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    private void startServer() throws IOException {
        System.out.println("Starting server on port " + serverSocket.getLocalPort() + " ...");
        while (!stop) {
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            if (checkSocket(socket, in, out)) {
                String clientName = handshake(socket, in, out);
                if (clientName != null) {
                    System.out.println("Connected to " + clientName);
                    new Receiver(socket, in, out, clientName, this).start();
                    stop = true; // stop server after connecting to single client
                    LOGGER.log(Level.INFO, "Server.startServer: Server stopped");
                }
            }
        }
    }

    /**
     * Check if socket is valid client
     * @return if socket is valid client
     */
    private boolean checkSocket(Socket socket, BufferedReader in, PrintWriter out) {
        // TODO: implement check socket
        return true;
    }

    /**
     * Exchange device names with server desktop
     * @return client device name if handshake was successful
     */
    private String handshake(Socket socket, BufferedReader in, PrintWriter out) {
        try {
            String clientName = in.readLine();
            String deviceName = InetAddress.getLocalHost().getHostName();
            out.println(deviceName);
            return clientName;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server.handshake: Handshake failed closing connection");
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            int port = 1234;
            Server server = new Server(port);

            try {
                server.startServer();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Server.main: IO exception occurred, server stopped");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server.main: Server failed to start");
        }
    }
}

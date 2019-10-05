package com.rfw.hotkey_server.net;

import com.rfw.hotkey_server.util.PacketHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Receiver extends Thread {
    private static final Logger LOGGER = Logger.getLogger(Receiver.class.getName());

    private Server server;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;

    private PacketHandler packetHandler;

    public volatile boolean stop = false;

    Receiver(Socket socket, BufferedReader in, PrintWriter out, String clientName, Server server) {
        this.server = server;
        this.in = in;
        this.out = out;
        this.clientName = clientName;
        this.socket = socket;
        this.packetHandler = new PacketHandler();
    }

    @Override
    public void run() {
        while (!stop && socket.isConnected()) {
            try {
                String line = in.readLine();
                if (line == null) { // check if the client is disconnected
                    LOGGER.log(Level.INFO, "Receiver.run: client disconnected, closing socket ...");
                    break;
                }
                handleMessage(line);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Receiver.run: IO error closing connection");
                break;
            }
        }
    }

    private void handleMessage(String message) {
        packetHandler.handle(message);
    }
}

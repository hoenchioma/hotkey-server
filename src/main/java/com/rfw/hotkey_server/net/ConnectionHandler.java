package com.rfw.hotkey_server.net;

import com.rfw.hotkey_server.util.PacketHandler;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandler extends Thread {
    private static final Logger LOGGER = Logger.getLogger(ConnectionHandler.class.getName());

    private Server server;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;

    private PacketHandler packetHandler;

    public volatile boolean stop = false;

    ConnectionHandler(Socket socket, BufferedReader in, PrintWriter out, String clientName, Server server) {
        this.server = server;
        this.in = in;
        this.out = out;
        this.clientName = clientName;
        this.socket = socket;
        this.packetHandler = new PacketHandler(this);
    }

    @Override
    public void run() {
        while (!stop && socket.isConnected()) {
            try {
                String line = in.readLine();
                if (line == null) { // check if the client is disconnected
                    LOGGER.log(Level.INFO, "ConnectionHandler.run: client disconnected, closing socket ...");
                    break;
                }
                handleMessage(line);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "ConnectionHandler.run: IO error closing connection");
                break;
            }
        }
    }

    private void handleMessage(String message) {
        packetHandler.handle(message);
    }

    public void sendPacket(JSONObject packet) {
        out.println(packet);
    }
}

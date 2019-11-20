package com.rfw.hotkey_server.net;

import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandler extends Thread {
    private static final Logger LOGGER = Logger.getLogger(ConnectionHandler.class.getName());

    private Server server;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;

    private PacketHandler packetHandler;

    private volatile boolean stop = true;

    ConnectionHandler(BufferedReader in, PrintWriter out, String clientName, Server server) throws AWTException {
        this.server = server;
        this.in = in;
        this.out = out;
        this.clientName = clientName;
        this.packetHandler = new PacketHandler(this);
    }

    void startConnection() {
        server.onConnect(clientName); // call on connect to notify wiFiServer
        stop = false;
        start();
    }

    void stopConnection() {
        stop = true;
        packetHandler.exit();
        server.removeConnection();
        server.onDisconnect(); // call on disconnect method to notify wiFiServer
    }

    boolean isActive() {
        return !stop;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                String line = in.readLine();
                if (line == null) { // check if the client is disconnected
                    LOGGER.log(Level.INFO, "ConnectionHandler.run: client disconnected, closing socket ...\n");
                    break;
                }
                handleMessage(line);
            } catch (IOException e) {
                server.onError(e);
                LOGGER.log(Level.SEVERE, "ConnectionHandler.run: IO error closing connection\n");
                break;
            }
        }
        stopConnection();
    }

    private void handleMessage(String message) {
        packetHandler.handle(message);
    }

    /**
     * to be called in response to a sendAndReceivePacket request
     * (sends a single packet to client)
     *
     * @param packet a JSONObject containing the packet
     */
    void sendPacket(JSONObject packet) {
        out.println(packet);
    }
}

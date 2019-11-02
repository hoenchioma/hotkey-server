package com.rfw.hotkey_server.net;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WiFiConnectionHandler extends Thread implements ConnectionHandler {
    private static final Logger LOGGER = Logger.getLogger(WiFiConnectionHandler.class.getName());

    private Server server;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;

    private PacketHandler packetHandler;

    private volatile boolean stop = false;

    WiFiConnectionHandler(Socket socket, BufferedReader in, PrintWriter out, String clientName, Server server) {
        this.server = server;
        this.in = in;
        this.out = out;
        this.clientName = clientName;
        this.socket = socket;
        this.packetHandler = new PacketHandler(this);
    }

    @Override
    public void startConnection() {
        server.onConnect(clientName, getConnectionType()); // call on connect to notify server
        start();
    }

    @Override
    public void stopConnection() {
        stop = true;
        packetHandler.exit();
        server.connection = null;
        server.onDisconnect(); // call on disconnect method to notify server
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.WIFI;
    }

    @Override
    public void run() {
        while (!stop && socket.isConnected()) {
            try {
                String line = in.readLine();
                if (line == null) { // check if the client is disconnected
                    LOGGER.log(Level.INFO, "WiFiConnectionHandler.run: client disconnected, closing socket ...\n");
                    break;
                }
                handleMessage(line);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "WiFiConnectionHandler.run: IO error closing connection\n");
                break;
            }
        }
        stopConnection();
    }

    private void handleMessage(String message) {
        packetHandler.handle(message);
    }

    public void sendPacket(JSONObject packet) {
        out.println(packet);
    }
}

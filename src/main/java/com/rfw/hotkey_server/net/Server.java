package com.rfw.hotkey_server.net;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.rfw.hotkey_server.util.Utils.*;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private ServerSocket serverSocket;
    private int port = 0;
    private volatile boolean stop = true;

    ConnectionHandler connection = null;

    public Server() { }

    public Server(int port) {
        this.port = port;
    }

    public ConnectionHandler getConnection() {
        return connection;
    }

    public int getLocalPort() {
        return serverSocket.getLocalPort();
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);

        LOGGER.log(Level.INFO, "Server.start: " + String.format(
                "Server started \nIP Address: %s\nPort: %d\n",
                getLocalIpAddress(), getLocalPort()
        ));

        stop = false;
        new Thread(this::connectionHandlingLoop).start();
    }

    public void stop() {
        stop = true;
        if (connection != null) {
            connection.stopConnection();
        }
    }

    public boolean isRunning() {
        return !stop;
    }

    /**
     * method called on connecting to a device
     * (meant to be overridden)
     */
    protected void onConnect(String deviceName, ConnectionType type) {}

    /**
     * method called on disconnecting from a device
     * (meant to be overridden)
     */
    protected void onDisconnect() {}

    private void connectionHandlingLoop() {
        while (!stop) {
            try {
                Socket socket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                new Thread(() -> handleConnection(socket, in, out)).start(); // start a new thread to handle the connection
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Server.connectionHandlingLoop: error handling connection");
                e.printStackTrace();
            }
        }
    }

    private void handleConnection(Socket socket, BufferedReader in, PrintWriter out) {
        try {
            String message = in.readLine();
            JSONObject receivedPacket = new JSONObject(new JSONTokener(message));
            String packetType = receivedPacket.getString("type");

            switch (packetType) {
                case "connectionRequest":
                    String clientName = receivedPacket.getString("deviceName");
                    String connectionType = receivedPacket.getString("connectionType");

                    switch (connectionType) {
                        case "normal":
                            JSONObject responsePacket = new JSONObject();
                            responsePacket.put("type", "connectionResponse");
                            responsePacket.put("deviceName", getDeviceName());

                            try {
                                if (connection != null) throw new IllegalStateException("Server already connected");
                                connection = new WiFiConnectionHandler(socket, in, out, clientName, this);
                                connection.startConnection();

                                LOGGER.log(Level.INFO, "Server.handleConnection: " + String.format(
                                        "Connected to %s [%s]\n", clientName, getRemoteSocketAddressAndPort(socket)
                                ));
                                responsePacket.put("success", true);
                            } catch (Exception e) {
                                LOGGER.log(Level.SEVERE, "Server.handleConnection: " + String.format(
                                        "failed to start connection\n[%s, %s]\n",
                                        clientName,
                                        getRemoteSocketAddressAndPort(socket)
                                ));
                                responsePacket.put("success", false);
                            }

                            out.println(responsePacket);

                            break;

                        default:
                            LOGGER.log(Level.SEVERE, "Server.handleConnection: " + String.format("unknown connection type (%s)\n", connectionType));
                    }
                    break;

                case "ping":
                    JSONObject responsePacket = getServerInfoPacket();
                    responsePacket.put("type", "pingBack");
                    out.println(responsePacket);
                    break;

                default:
                    LOGGER.log(Level.SEVERE, "Server.handleConnection: " + String.format("unknown connection packet type (%s)\n", packetType));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server.handleConnection: error handling connection\n");
        }
    }

    public String getQRCodeInfo() {
        JSONObject code = getServerInfoPacket();
        code.put("type", "qrCode");
        return code.toString();
    }

    private JSONObject getServerInfoPacket() {
        JSONObject packet = new JSONObject();
        packet.put("deviceName", getDeviceName());
        packet.put("ipAddress", getLocalIpAddress());
        packet.put("port", getLocalPort());
        return packet;
    }

    public static void main(String[] args) {
        Server server;
        if (args.length >= 1) {
            int port = Integer.parseInt(args[0]);
            server = new Server(port);
        } else { // if no port specified bind server to any available port
            server = new Server();
        }

        try {
            server.start();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server.main: Server failed to start\n");
        }
    }
}

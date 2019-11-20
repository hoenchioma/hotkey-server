package com.rfw.hotkey_server.net;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.rfw.hotkey_server.util.Device.getLocalIpAddress;
import static com.rfw.hotkey_server.util.Device.getRemoteSocketAddressAndPort;

/**
 * Server for hosting WiFi/LAN connection
 *
 * @author Raheen Hassan
 */
public class WiFiServer implements Server {
    private static final Logger LOGGER = Logger.getLogger(WiFiServer.class.getName());

    private ServerSocket serverSocket;
    private int port = 0; // port = 0 means system automatically assigns port
    private volatile boolean stop = true;

    private ConnectionHandler connection = null;

    public WiFiServer() {}

    public WiFiServer(int port) {
        this.port = port;
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.WIFI;
    }

    @Override
    public ConnectionHandler getConnection() {
        return connection;
    }

    @Override
    public void setConnection(ConnectionHandler connection) {
        this.connection = connection;
    }

    public int getLocalPort() {
        return serverSocket.getLocalPort();
    }

    @Override
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);

        LOGGER.log(Level.INFO, "WiFiServer.start: " + String.format(
                "WiFiServer started \nIP Address: %s\nPort: %d\n",
                getLocalIpAddress(), getLocalPort()
        ));

        stop = false;
        new Thread(this::connectionHandlingLoop).start();
    }

    @Override
    public void stop() throws IOException {
        stop = true;
        if (connection != null) {
            connection.stopConnection();
        }
        serverSocket.close();
    }

    @Override
    public boolean isRunning() {
        return !stop;
    }

    private void connectionHandlingLoop() {
        while (!stop) {
            try {
                Socket socket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                new Thread(() -> handleConnection(in, out, getRemoteSocketAddressAndPort(socket))).start(); // start a new thread to handle the connection
            } catch (SocketException e) {
                LOGGER.log(Level.WARNING, "WiFiServer.connectionHandlingLoop: " + e.getMessage());
            } catch (IOException e) {
                onError(e);
                LOGGER.log(Level.SEVERE, "WiFiServer.connectionHandlingLoop: error handling connection");
                e.printStackTrace();
            }
        }
    }

    @Override
    public JSONObject getServerInfoPacket() {
        return Server.super.getServerInfoPacket()
                .put("ipAddress", getLocalIpAddress())
                .put("port", getLocalPort());
    }

    public static void main(String[] args) {
        WiFiServer server;
        if (args.length >= 1) {
            int port = Integer.parseInt(args[0]);
            server = new WiFiServer(port);
        } else { // if no port specified bind server to any available port
            server = new WiFiServer();
        }

        try {
            server.start();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "WiFiServer.main: WiFiServer failed to start\n");
            e.printStackTrace();
        }
    }
}

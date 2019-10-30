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
    public volatile boolean stop = false;

    private Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    private Server() throws IOException {
        serverSocket = new ServerSocket(0);
    }
    
    public void stop() {
        stop = true;
    }
    
    public int getLocalPort() {
        return serverSocket.getLocalPort();
    }

    private void startServer() throws IOException {
        LOGGER.log(Level.INFO, "Server.startServer: " + String.format(
                "Server started \nIP Address: %s\nPort: %d\n",
                getLocalIpAddress(), getLocalPort()
        ));

        new Thread(this::generateQRCode).start(); // generate a qr code in a different thread

        connectionHandlingLoop();
    }

    private void connectionHandlingLoop() throws IOException {
        while (!stop) {
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> handleConnection(socket, in, out)).start(); // start a new thread to handle the connection
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
                                new ConnectionHandler(socket, in, out, clientName, this).start();
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

    private void generateQRCode() {
        JSONObject code = getServerInfoPacket();
        code.put("type", "qrCode");

        ByteArrayOutputStream stream = QRCode.from(code.toString()).to(ImageType.JPG).withSize(500, 500).stream();
        byte[] bytes = stream.toByteArray();
        ImageIcon icon = new ImageIcon(bytes);

        JFrame frame = new JFrame();
        frame.setTitle("Scan this");
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JSONObject getServerInfoPacket() {
        JSONObject packet = new JSONObject();
        packet.put("deviceName", getDeviceName());
        packet.put("ipAddress", getLocalIpAddress());
        packet.put("port", getLocalPort());
        return packet;
    }

    public static void main(String[] args) {
        try {
            Server server;
            if (args.length >= 1) {
                int port = Integer.parseInt(args[0]);
                server = new Server(port);
            } else { // if no port specified bind server to any available port
                server = new Server();
            }

            try {
                server.startServer();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Server.main: IO exception occurred, server stopped\n");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server.main: Server failed to start\n");
        }
    }
}

package com.rfw.hotkey_server.control;

import org.imgscalr.Scalr;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.rfw.hotkey_server.util.Utils.compressToJPEG;

public class LiveScreenController {
    private static final Logger LOGGER = Logger.getLogger(LiveScreenController.class.getName());

    public static final float JPEG_COMPRESSION_QUALITY = 0.22f;

    private Robot robot;

    private LiveScreenSender liveScreenSender;

    public LiveScreenController() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            // TODO: handle robot exception
        }
    }

    public void stop() {
        if (liveScreenSender != null) {
            liveScreenSender.running = false;
        }
    }

    public void handleIncomingPacket(JSONObject packet) {
        switch (packet.getString("command")) {
            case "start":
                String ipAddress = packet.getString("ipAddress");
                int port = packet.getInt("port");
                int screenSizeX = packet.getInt("screenSizeX");
                int screenSizeY = packet.getInt("screenSizeY");
                try {
                    liveScreenSender = new LiveScreenSender(ipAddress, port, screenSizeX, screenSizeY);
                    liveScreenSender.start();
                } catch (SocketException | UnknownHostException e) {
                    LOGGER.log(Level.SEVERE, "LiveScreenController.handleIncomingPacket: error starting LiveScreenSender");
                    e.printStackTrace();
                }
                break;
            case "stop":
                if (liveScreenSender != null) {
                    liveScreenSender.running = false;
                }
                break;
            default:
                LOGGER.log(Level.SEVERE, "LiveScreenController.handleIncomingPacket: invalid packet");
        }
    }

    private class LiveScreenSender extends Thread {
        DatagramSocket socket;
        InetAddress address;
        int port;
        int screenSizeX, screenSizeY;

        volatile boolean running = false;

        public LiveScreenSender(String ipAddress, int port, int screenSizeX, int screenSizeY)
                throws SocketException, UnknownHostException {
            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(ipAddress);
            this.port = port;
            this.screenSizeX = screenSizeX;
            this.screenSizeY = screenSizeY;
        }

        @Override
        public synchronized void start() {
            running = true;
            super.start();
        }

        @Override
        public void run() {
            while (running) {
                try {
                    BufferedImage screenshot = robot.createScreenCapture(
                            new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
//                    LOGGER.log(Level.INFO, "LiveScreenSender.run: " + screenshot.getWidth() + " " + screenshot.getHeight());
//                    LOGGER.log(Level.INFO, "LiveScreenSender.run: " + screenSizeX + " " + screenSizeY);
                    BufferedImage resizedSS = Scalr.resize(screenshot,
                            Scalr.Method.BALANCED, screenSizeX, screenSizeY);
                    byte[] imageBuff = compressToJPEG(resizedSS, JPEG_COMPRESSION_QUALITY);

//                    LOGGER.log(Level.INFO, "LiveScreenSender.run: image size: " + imageBuff.length);

                    // TODO: fix message too long problem

                    DatagramPacket packet = new DatagramPacket(imageBuff, imageBuff.length, address, port);
                    socket.send(packet);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "LiveScreenSender.run: error while sending screenshot");
                    e.printStackTrace();
//                    break;
                }
            }
            try {
                // send good byte package (to signal client to stop receiving)
                DatagramPacket packet = new DatagramPacket(new byte[0], 0, address, port); // byte buff of 0 size
                socket.send(packet);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "LiveScreenSender.run: error sending good byte package");
                e.printStackTrace();
            }
            running = false;
        }
    }
}

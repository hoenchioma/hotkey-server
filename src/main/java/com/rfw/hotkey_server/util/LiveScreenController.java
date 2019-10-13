package com.rfw.hotkey_server.util;

import org.imgscalr.Scalr;
import org.json.JSONObject;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LiveScreenController {
    private static final Logger LOGGER = Logger.getLogger(LiveScreenController.class.getName());

    private Robot robot;

    LiveScreenSender liveScreenSender;

    public LiveScreenController() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            // TODO: handle robot exception
        }
    }

    void handleIncomingPacket(JSONObject packet) {
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
                    byte[] imageBuff = compressToJPEG(resizedSS, 0.22f);

                    // TODO: fix message too long problem

//                    LOGGER.log(Level.INFO, "LiveScreenSender.run: image size: " + imageBuff.length);
//                    intToByteArray(25, sendBuff, 0);

                    DatagramPacket packet = new DatagramPacket(imageBuff, imageBuff.length, address, port);
//                    DatagramPacket packet = new DatagramPacket(sendBuff, 4, address, port);
                    socket.send(packet);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "LiveScreenSender.run: error while sending screenshot");
                    e.printStackTrace();
                    break;
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
        }
    }

    private static void intToByteArray(int value, byte[] res, int offset) {
        res[offset] = (byte)(value >> 24);
        res[offset + 1] = (byte)(value >> 16);
        res[offset + 2] = (byte)(value >> 8);
        res[offset + 3] = (byte)value;
    }

    private static int intFromByteArray(byte[] bytes, int offset) {
        return bytes[offset] << 24
                | (bytes[offset + 1] & 0xFF) << 16
                | (bytes[offset + 2] & 0xFF) << 8
                | (bytes[offset + 3] & 0xFF);
    }

    private byte[] compressToJPEG(BufferedImage image, float quality) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        // compress to a given quality
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        writer.write(null, new IIOImage(image, null, null), param);

        ios.close();
        writer.dispose();

        return out.toByteArray();
    }
}

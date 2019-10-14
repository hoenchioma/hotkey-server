package com.rfw.hotkey_server.control;

import org.imgscalr.Scalr;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.rfw.hotkey_server.util.Utils.compressToJPEG;

public class LiveScreenController {
    private static final Logger LOGGER = Logger.getLogger(LiveScreenController.class.getName());

    public static final int CONNECTION_TIMEOUT = 5000;
    public static final float JPEG_COMPRESSION_QUALITY = 0.25f;

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
                float targetFps = packet.getFloat("fps");
                try {
                    liveScreenSender = new LiveScreenSender(ipAddress, port, screenSizeX, screenSizeY, targetFps);
                    liveScreenSender.start();
                } catch (SocketTimeoutException e) {
                    LOGGER.log(Level.SEVERE, "LiveScreenController.handleIncomingPacket: socket connection timed out");
                } catch (IOException e) {
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
        Socket socket;
        DataOutputStream out;

        String ipAddress;
        int port;
        int screenSizeX, screenSizeY;

        float targetFps;

        volatile boolean running = false;

        public LiveScreenSender(String ipAddress, int port, int screenSizeX, int screenSizeY, float targetFps)
                throws IOException {
            this.ipAddress = ipAddress;
            this.port = port;
            this.screenSizeX = screenSizeX;
            this.screenSizeY = screenSizeY;
            this.targetFps = targetFps;

            socket = new Socket();
            socket.connect(new InetSocketAddress(ipAddress, port), CONNECTION_TIMEOUT);
            out = new DataOutputStream(socket.getOutputStream());
        }

        @Override
        public synchronized void start() {
            running = true;
            super.start();
        }
        
        private void exit() throws IOException {
            running = false;
            out.close();
            socket.close();
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

                    out.writeInt(imageBuff.length);
                    out.write(imageBuff);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "LiveScreenSender.run: error while sending screenshot");
                    LOGGER.log(Level.INFO, "LiveScreenSender.run: exiting sender ...");
                    e.printStackTrace();
                    break;
                }
//                try {
//                    Thread.sleep((int) (1000.0 / targetFps));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
            try {
                // send good byte package (to signal client to stop receiving)
                out.writeInt(0);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "LiveScreenSender.run: error sending good byte package");
                e.printStackTrace();
            }
            try {
                exit();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "LiveScreenSender.run: error exiting");
                e.printStackTrace();
            }
        }
    }
}

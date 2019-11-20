package com.rfw.hotkey_server.control;

import org.imgscalr.Scalr;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.rfw.hotkey_server.util.Image.*;

public class LiveScreenController {
    private static final Logger LOGGER = Logger.getLogger(LiveScreenController.class.getName());

    private static final int CONNECTION_TIMEOUT = 1000;
    private static final int CURSOR_SIZE = 50;

    private Robot robot;

    private LiveScreenSender liveScreenSender;

    public LiveScreenController() throws AWTException {
        robot = new Robot();
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
                float compressRatio = packet.getFloat("compressRatio");
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(ipAddress, port), CONNECTION_TIMEOUT);
                    liveScreenSender = new LiveScreenSender(socket, screenSizeX, screenSizeY, targetFps, compressRatio);
                    liveScreenSender.start();
                } catch (SocketTimeoutException e) {
                    LOGGER.log(Level.SEVERE, "LiveScreenController.handleIncomingPacket: socket connection timed out\n");
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "LiveScreenController.handleIncomingPacket: error starting LiveScreenSender\n");
                    e.printStackTrace();
                }
                break;
            case "stop":
                if (liveScreenSender != null) {
                    liveScreenSender.running = false;
                }
                break;
            default:
                LOGGER.log(Level.SEVERE, "LiveScreenController.handleIncomingPacket: invalid packet\n");
        }
    }

    private class LiveScreenSender extends Thread {
        Socket socket;
        DataOutputStream out;

        int screenSizeX, screenSizeY;
        float fps;
        float compressRatio;

        volatile boolean running = false;

        LiveScreenSender(Socket socket,
                         int screenSizeX, int screenSizeY,
                         float fps, float compressRatio)
                throws IOException {

            this.screenSizeX = screenSizeX;
            this.screenSizeY = screenSizeY;
            this.fps = fps;
            this.compressRatio = compressRatio;

            this.out = new DataOutputStream(socket.getOutputStream());
        }

        @Override
        public synchronized void start() {
            running = true;
            super.start();
        }

        private void exit() throws IOException {
            running = false;
            if (out != null) out.close();
            if (socket != null) socket.close();
        }

        @Override
        public void run() {
            BufferedImage cursor = null;
            try {
                 cursor = ImageIO.read(new File(
                        getClass().getClassLoader().getResource("images/cursor.png").getFile()
                ));
                 cursor = Scalr.resize(cursor, Scalr.Method.BALANCED, CURSOR_SIZE, CURSOR_SIZE);
            } catch (IOException | NullPointerException e) {
                LOGGER.log(Level.SEVERE, "LiveScreenSender.run: error reading cursor image file");
                e.printStackTrace();
            }
            while (running) {
                try {
                    // capture screenshot
                    BufferedImage screenshot = robot.createScreenCapture(
                            new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

                    // place a fake cursor icon on screenshot
                    if (cursor != null) {
                        int cursorPosX = MouseInfo.getPointerInfo().getLocation().x;
                        int cursorPosY = MouseInfo.getPointerInfo().getLocation().y;
                        addImage(screenshot, cursor, 1.0f, cursorPosX, cursorPosY);
                    }

                    // resize screenshot according to target device
                    BufferedImage resizedSS = Scalr.resize(screenshot,
                            Scalr.Method.BALANCED, screenSizeX, screenSizeY);


                    byte[] imageBuff = compressRatio == 1.0f ? getPNG(resizedSS) : compressToJPEG(resizedSS, compressRatio);

//                    LOGGER.log(Level.INFO, "LiveScreenSender.run: image size: " + imageBuff.length + " " + (int) imageBuff.length);

                    out.writeInt(imageBuff.length);
                    out.flush();
                    out.write(imageBuff);
                    out.flush();

                } catch (IOException e) {
                    if (running) { // if client is supposed to be disconnected don't show error
                        LOGGER.log(Level.SEVERE, "LiveScreenSender.run: error while sending screenshot\n");
                        e.printStackTrace();
                    }
                    LOGGER.log(Level.INFO, "LiveScreenSender.run: exiting sender ...\n");
                    break;
                }
                try {
                    Thread.sleep((int) (1000.0 / fps));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            try {
//                // send good byte package (to signal client to stop receiving)
//                out.writeInt(0);
//            } catch (IOException e) {
//                LOGGER.log(Level.SEVERE, "LiveScreenSender.run: error sending good byte package");
//                e.printStackTrace();
//            }
            try {
                exit();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "LiveScreenSender.run: error exiting\n");
                e.printStackTrace();
            }
        }
    }
}

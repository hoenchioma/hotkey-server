package com.rfw.hotkey_server.util;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Utils {
    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

    /**
     * Get the local IP address of this device
     * @return IP address of device (in String format)
     */
    public static String getLocalIpAddress() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Utils.getLocalIpAddress: error getting local IP address");
        }
        return null;
    }

    /**
     * get the IP address and port of a remote device
     * @param socket the socket of the device whose IP we need to find
     * @return the IP address and port separated by colon (:) [e.g. "192.168.1.2:5884"]
     */
    public static String getRemoteSocketAddressAndPort(Socket socket) {
        return socket.getRemoteSocketAddress().toString().replace("/","");
    }

    /**
     * returns the name of the device (computer name)
     */
    public static String getDeviceName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * Convert int to byte array
     * @param value int to be converted
     * @param res byte array for placing output
     * @param offset offset for res (where in res the output is placed in)
     */
    public static void intToByteArray(int value, byte[] res, int offset) {
        res[offset] = (byte)(value >> 24);
        res[offset + 1] = (byte)(value >> 16);
        res[offset + 2] = (byte)(value >> 8);
        res[offset + 3] = (byte)value;
    }

    /**
     * Convert int to byte array
     * @param value int to be converted
     * @return required byte array
     */
    public static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >> 24),
                (byte)(value >> 16),
                (byte)(value >> 8),
                (byte)value};
    }

    /**
     * Convert byte array to int
     * @param bytes byte arr to be converted
     * @param offset offset for bytes (where in bytes is the input located)
     * @return the required int
     */
    public static int intFromByteArray(byte[] bytes, int offset) {
        return bytes[offset] << 24
                | (bytes[offset + 1] & 0xFF) << 16
                | (bytes[offset + 2] & 0xFF) << 8
                | (bytes[offset + 3] & 0xFF);
    }

    /**
     * Convert the image to JPEG
     * @param image BufferedImage to be converted
     * @param quality the quality setting in the JPEG conversion (0~1)
     * @return the byte array containing the JPEG image
     */
    public static byte[] compressToJPEG(BufferedImage image, float quality) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);
        ImageWriteParam param = writer.getDefaultWriteParam();

        // compress to a given quality
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);
        writer.write(null, new IIOImage(image, null, null), param);

        // close all open streams
        ios.close();
        writer.dispose();

        return out.toByteArray();
    }

    public static byte[] getPNG(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return out.toByteArray();
    }
}

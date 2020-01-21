package com.rfw.hotkey_server.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public final class Image {
    private static final Logger LOGGER = Logger.getLogger(Image.class.getName());

    /**
     * Convert the image to JPEG
     *
     * @param image   BufferedImage to be converted
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

    /**
     * Convert BufferedImage to PNG format (byte stream)
     *
     * @param image in BufferedImage form
     * @return byte array with image in PNG
     */
    public static byte[] getPNG(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return out.toByteArray();
    }

    /**
     * prints the contents of toDraw on on with the given opaque value.
     *
     * @param on BufferedImage on which toDraw is drawn
     * @param toDraw BufferedImage which is drawn on "on"
     * @param opaque opacity of drawing (0~1)
     * @param corX co-ordinate corX of where to draw toDraw
     * @param corY co-ordinate corY of where to draw toDraw
     */
    public static void addImage(BufferedImage on, BufferedImage toDraw, float opaque, int corX, int corY) {
        Graphics2D g2d = on.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opaque));
        g2d.drawImage(toDraw, corX, corY, null);
        g2d.dispose();
    }

    /**
     * Create a swing window to show a single image
     * @param image the image in byte array form
     */
    public static @Nonnull
    JFrame showImageInWindow(byte[] image, @Nullable String windowTitle) {
        JFrame frame = new JFrame();
        try {
            SwingUtilities.invokeAndWait(() -> {
                ImageIcon icon = new ImageIcon(image);
                if (windowTitle != null) frame.setTitle(windowTitle);
                JLabel label = new JLabel(icon);
                frame.add(label);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return frame;
    }
}

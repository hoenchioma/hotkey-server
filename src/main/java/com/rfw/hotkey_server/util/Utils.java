package com.rfw.hotkey_server.util;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.logging.Logger;

/**
 * Utility class containing useful functions
 *
 * @author Raheeb Hassan
 */
public final class Utils {
    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

    /**
     * Make a QR code image from a String
     *
     * @param code String representing the QR code
     * @return byte array representing the array
     */
    public static byte[] makeQRCode(String code, int imageSizeX, int imageSizeY, ImageType imageType) {
        return QRCode.from(code)
                .to(imageType)
                .withSize(imageSizeX, imageSizeY)
                .stream()
                .toByteArray();
    }

    /**
     * show the given string as a QR code (in swing window)
     *
     * @param code String to convert to a QR code
     */
    public static @Nonnull
    JFrame showQRCode(String code, int imageSizeX, int imageSizeY) {
        return Image.showImageInWindow(makeQRCode(code, imageSizeX, imageSizeY, ImageType.JPG), "Scan this");
    }
}

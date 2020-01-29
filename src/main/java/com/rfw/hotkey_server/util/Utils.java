package com.rfw.hotkey_server.util;

import com.rfw.hotkey_server.util.misc.PlatformException;
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

    /**
     * Get the lib file name (.dll or .so) from lib name
     *
     * @param libName library name
     * @return filename (with required extension)
     * @throws PlatformException if OS/CPU architecture not supported
     */
    public static String getLibFileName(String libName) throws PlatformException {
        final String osName = System.getProperty("os.name").toLowerCase();
        final String jreArch = System.getProperty("sun.arch.data.model");
        String fileName = libName; // filename and libName are same by default
        if (osName.contains("win")) { // windows system
            if (jreArch.equals("64")) fileName = libName + ".dll";
            else if (jreArch.equals("32")) throw new PlatformException("32 bit JRE not supported");
            else throw new PlatformException("Unknown JRE architecture");
        } else if (osName.contains("nix") | osName.contains("nux") | osName.contains("aix")) { // unix systems
            if (jreArch.equals("64")) fileName = "lib" + libName + ".so";
            else if (jreArch.equals("32")) throw new PlatformException("32 bit JRE not supported");
            else throw new PlatformException("Unknown JRE architecture");
        } else if (osName.contains("mac")) { // macOS system
            throw new PlatformException("MacOS not supported");
        } else {
            throw new PlatformException("Unsupported OS");
        }
        return fileName;
    }
}

package com.rfw.hotkey_server.commands;

import com.rfw.hotkey_server.util.Utils;
import com.rfw.hotkey_server.util.misc.NativeLibLoader;
import com.rfw.hotkey_server.util.misc.PlatformException;
import javafx.scene.control.Alert;

import java.io.IOException;

/**
 * JNI class for using native bridge to execute media keys
 *
 * @author Raheeb Hassan
 */
public class MediaKeys {
    static {
        try {
            loadLib();
        } catch (PlatformException | IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load media-keys library\nMedia keys functionality will not work").show();
            e.printStackTrace();
        }
    }

    // static flag indicating whether the native lib has been loaded
    private static boolean libLoaded = false;

    public static boolean isLibLoaded() {
        return libLoaded;
    }

    /**
     * load the MediaKeys JNI library
     *
     * @throws PlatformException unsupported OS/architecture
     * @throws IOException failed to load lib (file IO exception)
     */
    public static void loadLib() throws PlatformException, IOException {
        String libName = "media-keys";
        String fileName = Utils.getLibFileName(libName);
        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError e) {
            // if failed to load normally try loading from jar file
            try {
                NativeLibLoader.loadLibraryFromJar("/" + fileName);
            } catch (IOException ex) {
                throw new IOException("Failed to load lib from JAR", ex);
            }
        }
        libLoaded = true;
    }

    public static native void volumeMute();

    public static native void volumeDown();

    public static native void volumeUp();

    public static native void songPrevious();

    public static native void songNext();

    public static native void songPlayPause();
}

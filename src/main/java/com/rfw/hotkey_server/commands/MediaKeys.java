package com.rfw.hotkey_server.commands;

import com.rfw.hotkey_server.util.misc.NativeUtils;

import java.io.IOException;

/**
 * JNI class for using native bridge to execute media keys
 *
 * @author Raheeb Hassan
 */
public class MediaKeys {
    static {
        loadLibrary("media-keys");
    }

    /**
     * load the native library
     * (based on OS and JVM architecture)
     */
    private static void loadLibrary(String libName) {
        final String osName = System.getProperty("os.name").toLowerCase();
        final String jreArch = System.getProperty("sun.arch.data.model");
        String fileName = libName;
        if (osName.contains("win")) { // windows system
            if (jreArch.equals("32")) { // 32 bit jre
                libName += "-x86";
            } else if (jreArch.equals("64")) { // 64 bit jre
                libName += "-x64";
            } else {
                throw new IllegalStateException("Unknown JVM architecture");
            }
            fileName = libName + ".dll";
        } else if (osName.contains("nix") | osName.contains("nux") | osName.contains("aix")) { // unix systems
            if (jreArch.equals("32")) throw new IllegalStateException("32 bit JRE on Linux not supported");
            fileName = "lib" + libName + ".so";
        } else if (osName.contains("mac")) { // macOS system
            throw new IllegalStateException("MacOS not supported");
        } else {
            throw new IllegalStateException("Unsupported OS");
        }
        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError e) {
            try {
                NativeUtils.loadLibraryFromJar("/" + fileName);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static native void volumeMute();

    public static native void volumeDown();

    public static native void volumeUp();

    public static native void songPrevious();

    public static native void songNext();

    public static native void songPlayPause();
}

package com.rfw.hotkey_server.commands;

/**
 * JNI class for using native bridge to execute media keys
 */
public class MediaKeys {
    static {
        loadLibrary();
    }

    private static void loadLibrary() {
        final String libName = "media-keys";
        final String osName = System.getProperty("os.name").toLowerCase();
        final String jreArch = System.getProperty("sun.arch.data.model");
        if (osName.contains("win")) { // windows system
            if (jreArch.equals("32")) { // 32 bit jre
                System.loadLibrary(libName + "-x86");
            } else if (jreArch.equals("64")) { // 64 bit jre
                System.loadLibrary(libName + "-x64");
            } else {
                throw new IllegalStateException("Unknown JVM architecture");
            }
        } else if (osName.contains("nix") | osName.contains("nux") | osName.contains("aix")) { // unix systems
            if (jreArch.equals("32")) throw new IllegalStateException("32 bit JRE on Linux not supported");
            System.loadLibrary(libName);
        } else if (osName.contains("mac")) { // macOS system
            throw new IllegalStateException("MacOS not supported");
        } else {
            throw new IllegalStateException("Unsupported OS");
        }
    }

    public static native void volumeMute();

    public static native void volumeDown();

    public static native void volumeUp();

    public static native void songPrevious();

    public static native void songNext();

    public static native void songPlayPause();
}

package com.rfw.hotkey_server.commands;

/**
 * JNI class for using native bridge to execute media keys
 */
public class MediaKeys {
    static {
        String libName = "bridge";
        if (System.getProperty("os.name").contains("Windows")) {
            if (System.getProperty("sun.arch.data.model").equals("32")) {
                System.loadLibrary(libName + "-x86");
            } else if (System.getProperty("sun.arch.data.model").equals("64")) {
                System.loadLibrary(libName + "-x64");
            } else {
                throw new IllegalStateException("Unknown JVM architecture");
            }
        }
        // TODO: linux
    }

    public static native void volumeMute();

    public static native void volumeDown();

    public static native void volumeUp();

    public static native void songPrevious();

    public static native void songNext();

    public static native void songPlayPause();

    public static void main(String[] args) {
        songPlayPause();
        System.getProperties().list(System.out);
    }
}

package com.rfw.hotkey_server.commands;

/**
 * JNI class for using native bridge to execute media keys
 */
public class MediaKeys {
    static {
        System.loadLibrary("bridge");
    }

    public static native void volumeMute();

    public static native void volumeDown();

    public static native void volumeUp();

    public static native void songPrevious();

    public static native void songNext();

    public static native void songPlayPause();

//    public static void main(String[] args) {
//        songPlayPause();
//    }
}

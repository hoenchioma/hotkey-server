package com.rfw.hotkey_server.util.misc;

/**
 * An exception to indicate that this platform (OS/architecture) is not supported
 */
public class PlatformException extends Exception {
    public PlatformException() {
    }

    public PlatformException(String message) {
        super(message);
    }

    public PlatformException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlatformException(Throwable cause) {
        super(cause);
    }
}

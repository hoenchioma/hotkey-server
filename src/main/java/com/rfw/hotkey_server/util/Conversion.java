package com.rfw.hotkey_server.util;

import java.util.logging.Logger;

public final class Conversion {
    private static final Logger LOGGER = Logger.getLogger(Conversion.class.getName());

    /**
     * Convert int to byte array
     *
     * @param value  int to be converted
     * @param res    byte array for placing output
     * @param offset offset for res (where in res the output is placed in)
     */
    public static void intToByteArray(int value, byte[] res, int offset) {
        res[offset] = (byte) (value >> 24);
        res[offset + 1] = (byte) (value >> 16);
        res[offset + 2] = (byte) (value >> 8);
        res[offset + 3] = (byte) value;
    }

    /**
     * Convert int to byte array
     *
     * @param value int to be converted
     * @return required byte array
     */
    public static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value};
    }

    /**
     * Convert byte array to int
     *
     * @param bytes  byte arr to be converted
     * @param offset offset for bytes (where in bytes is the input located)
     * @return the required int
     */
    public static int intFromByteArray(byte[] bytes, int offset) {
        return bytes[offset] << 24
                | (bytes[offset + 1] & 0xFF) << 16
                | (bytes[offset + 2] & 0xFF) << 8
                | (bytes[offset + 3] & 0xFF);
    }
}

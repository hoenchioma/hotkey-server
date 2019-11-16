package com.rfw.hotkey_server.net;

import javax.annotation.Nonnull;

public enum ConnectionType {
    WIFI,
    BLUETOOTH,
    INTERNET;

    @Override
    public @Nonnull String toString() {
        switch (this) {
            case WIFI: return "WiFi";
            case BLUETOOTH: return "Bluetooth";
            case INTERNET: return "Internet";
            default: throw new IllegalArgumentException();
        }
    }

    public @Nonnull String toCamelCaseString() {
        switch (this) {
            case WIFI: return "wiFi";
            case BLUETOOTH: return "bluetooth";
            case INTERNET: return "internet";
            default: throw new IllegalArgumentException();
        }
    }
}

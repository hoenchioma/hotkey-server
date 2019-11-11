package com.rfw.hotkey_server.net;

public enum ConnectionType {
    WIFI,
    BLUETOOTH,
    INTERNET;

    @Override
    public String toString() {
        switch (this) {
            case WIFI: return "WiFi";
            case BLUETOOTH: return "Bluetooth";
            case INTERNET: return "Internet";
            default: throw new IllegalArgumentException();
        }
    }
}

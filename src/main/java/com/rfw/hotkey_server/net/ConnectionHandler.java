package com.rfw.hotkey_server.net;

public interface ConnectionHandler {

    void startConnection();

    void stopConnection();

    ConnectionType getConnectionType();
}

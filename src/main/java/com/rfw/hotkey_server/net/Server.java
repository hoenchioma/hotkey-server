package com.rfw.hotkey_server.net;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.rfw.hotkey_server.util.Device.getDeviceName;

/**
 * Server for hosting connection
 *
 * @author Raheeb Hassan
 */
public interface Server {
    Logger LOGGER = Logger.getLogger(Server.class.getName());

    // unique identifier String for server
    String SERVER_UUID = "8fbdf1a6-1185-43a7-952a-3f38f6af0c36";
    // version number for server (only changed for major changes)
    int SERVER_VERSION = 2;

    ConnectionType getConnectionType();

    ConnectionHandler getConnection();

    void setConnection(ConnectionHandler connection);

    default void removeConnection() {
        setConnection(null);
    }

    void start() throws IOException;

    void stop() throws IOException;

    boolean isRunning();

    /**
     * Get a string to form connection QR code
     */
    default String getQRCodeInfo() {
        if (!isRunning()) throw new IllegalStateException();
        return getServerInfoPacket()
                .put("type", "qrCode")
                .toString();
    }

    /**
     * Returns a JSON packet containing info about server
     *
     * @return JSONObject containing the info
     */
    default JSONObject getServerInfoPacket() {
        return new JSONObject()
                .put("connectionType", getConnectionType().toCamelCaseString())
                .put("deviceName", getDeviceName());
//                .put("serverUuid", SERVER_UUID)
//                .put("serverVersion", SERVER_VERSION);
    }

    /**
     * method called on connecting to a device
     * (meant to be overridden)
     */
    default void onConnect(String deviceName) {}

    /**
     * method called on disconnecting from a device
     * (meant to be overridden)
     */
    default void onDisconnect() {}

    /**
     * method called when an error occurs
     * @param exception exception that was thrown
     * (meant to be overridden)
     */
    default void onError(@Nullable Exception exception) {}

    /**
     * Static method for handling/initiating a connection
     * (this automatically starts a ClientHandler to handle the connection)
     *
     * @param in         input stream
     * @param out        output stream
     * @param identifier String to uniquely identify the client (address, port, UUID, etc.)
     */
    default void handleConnection(BufferedReader in,
                                  PrintWriter out,
                                  @Nullable String identifier) {
        try {
            String message = in.readLine();
            JSONObject receivedPacket = new JSONObject(new JSONTokener(message));
            String packetType = receivedPacket.getString("type");

            if (packetType.equals("connectionRequest")) {
                String clientName = receivedPacket.getString("deviceName");
//                String connectionType = receivedPacket.getString("connectionType");

                String serverUuid = receivedPacket.getString("serverUuid");
                int serverVersion = receivedPacket.getInt("serverVersion");

                if (!serverUuid.equals(SERVER_UUID) || serverVersion != SERVER_VERSION) {
                    LOGGER.log(Level.SEVERE, "Server.handleConnection: client mismatch with server");
                    throw new IllegalArgumentException("Wrong server uuid and/or version");
                }

                JSONObject responsePacket = new JSONObject()
                        .put("type", "connectionResponse")
                        .put("deviceName", getDeviceName())
                        .put("serverUuid", SERVER_UUID)
                        .put("serverVersion", SERVER_VERSION);

                try {
                    if (getConnection() != null) throw new IllegalStateException("Server already connected");
                    setConnection(new ConnectionHandler(in, out, clientName, this));
                    getConnection().startConnection();

                    LOGGER.log(Level.INFO, "Server.handleConnection: " + String.format(
                            "Connected to %s [%s]\n", clientName, identifier
                    ));
                    responsePacket.put("success", true);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Server.handleConnection: " + String.format(
                            "failed to start connection\n[%s, %s]\n",
                            clientName,
                            identifier
                    ));
                    responsePacket.put("success", false);
                    throw new IllegalStateException(e);
                } finally {
                    out.println(responsePacket);
                }

            } else if (packetType.equals("ping")) {
                JSONObject responsePacket = getServerInfoPacket();
                responsePacket.put("type", "pingBack");
                out.println(responsePacket);

            } else {
                LOGGER.log(Level.SEVERE, "Server.handleConnection: " + String.format("unknown connection packet type (%s)\n", packetType));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException | IllegalArgumentException | IllegalStateException e) {
            onError(e);
            LOGGER.log(Level.SEVERE, "Server.handleConnection: error handling connection\n");
        }
    }
}

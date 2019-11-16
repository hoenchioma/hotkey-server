package com.rfw.hotkey_server.net;

import org.json.JSONObject;

import javax.annotation.Nullable;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.rfw.hotkey_server.util.Utils.showQRCode;

public class BluetoothServer implements Server {
    private static final Logger LOGGER = Logger.getLogger(BluetoothServer.class.getName());

    // unique UUID for the client to identify the server
    private static final String BLUETOOTH_SERVICE_UUID = "35ba7039-f3f2-4617-91f9-64c9c56bb437";
    private static final String BLUETOOTH_SERVICE_NAME = "Hotkey Bluetooth Server";

    private StreamConnectionNotifier streamConnectionNotifier = null;

    private volatile boolean stop = true;
    private ConnectionHandler connection = null;

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.BLUETOOTH;
    }

    @Override
    public ConnectionHandler getConnection() {
        return connection;
    }

    @Override
    public void setConnection(ConnectionHandler connection) {
        this.connection = connection;
    }

    public static boolean isBluetoothEnabled() {
        return LocalDevice.isPowerOn();
    }

    public static String getFriendlyName() throws BluetoothStateException {
        return LocalDevice.getLocalDevice().getFriendlyName();
    }

    public String getBluetoothAddress() throws BluetoothStateException {
        String rawAddress = LocalDevice.getLocalDevice().getBluetoothAddress();
        return colonifyAddress(rawAddress);
    }

    /**
     * convert raw (hex) address (mac/bt address) to colon separated form
     * @param rawAddress address string (not colon separated) (12 digit hex value)
     * @return address string colon separated
     */
    private String colonifyAddress(String rawAddress) {
        assert rawAddress.length() == 12;
        StringBuilder address = new StringBuilder();
        address.append(rawAddress.charAt(0));
        address.append(rawAddress.charAt(1));
        for (int i = 2; i < rawAddress.length(); i += 2) {
            address.append(':');
            address.append(rawAddress.charAt(i));
            address.append(rawAddress.charAt(i+1));
        }
        return address.toString();
    }

    @Override
    public void start() throws IOException {
        UUID uuid = new UUID(BLUETOOTH_SERVICE_UUID.replace("-", ""), false); // remove the "-"s from UUID
        String connectionString = String.format("btspp://localhost:%s;name=%s", uuid.toString(), BLUETOOTH_SERVICE_NAME);
        streamConnectionNotifier = (StreamConnectionNotifier) Connector.open(connectionString);

        LOGGER.log(Level.INFO, "BluetoothServer.start: " + String.format(
                "BluetoothServer started ...\nBluetooth name: %s\nBluetooth address: %s\n",
                LocalDevice.getLocalDevice().getFriendlyName(),
                getBluetoothAddress()
        ));

        stop = false;
        new Thread(this::connectionHandlingLoop).start();
    }

    @Override
    public void stop() throws IOException {
        stop = true;
        if (connection != null) {
            connection.stopConnection();
        }
        streamConnectionNotifier.close();
    }

    @Override
    public boolean isRunning() {
        return !stop;
    }

    @Override
    public @Nullable JSONObject getServerInfoPacket() {
        try {
            return Server.super.getServerInfoPacket()
//                    .put("bluetoothDeviceName", LocalDevice.getLocalDevice().getFriendlyName())
                    .put("bluetoothAddress", getBluetoothAddress())
                    .put("bluetoothServiceUuid", BLUETOOTH_SERVICE_UUID);
        } catch (BluetoothStateException e) {
            e.printStackTrace();
            LOGGER.log(Level.INFO, "BluetoothServer.getServerInfoPacket: bluetooth is probably not turned on ...");
            return null;
        }
    }

    private void connectionHandlingLoop() {
        while (!stop) {
            try {
                StreamConnection connection = streamConnectionNotifier.acceptAndOpen();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.openInputStream()));
                PrintWriter out = new PrintWriter(connection.openOutputStream(), true);

                RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
                String identifier = dev.getFriendlyName(true) + ", " + dev.getBluetoothAddress();

                new Thread(() -> handleConnection(in, out, identifier)).start(); // start a new thread to handle the connection
            } catch (InterruptedIOException e) {
                LOGGER.log(Level.WARNING, "BluetoothServer.connectionHandlingLoop: " + e.getMessage());
            } catch (IOException e) {
                onError(e);
                LOGGER.log(Level.SEVERE, "BluetoothServer.connectionHandlingLoop: error handling connection");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        BluetoothServer server = new BluetoothServer();
        try {
            server.start();
            showQRCode(server.getQRCodeInfo(), 500, 500);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "BluetoothServer.main: BluetoothServer failed to start\n");
            e.printStackTrace();
        }
    }
}

package com.rfw.hotkey_server.util;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Device {
    private static final Logger LOGGER = Logger.getLogger(Device.class.getName());

    /**
     * Get the local IP address of this device
     *
     * @return IP address of device (in String format)
     */
    public static @Nullable
    String getLocalIpAddress() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Utils.getLocalIpAddress: error getting local IP address" + String.format(" [%s]", e.getMessage()) + "\n");
        }
        return null;
    }

    /**
     * get the IP address and port of a remote device
     *
     * @param socket the socket of the device whose IP we need to find
     * @return the IP address and port separated by colon (:) [e.g. "192.168.1.2:5884"]
     */
    public static String getRemoteSocketAddressAndPort(Socket socket) {
        return socket.getRemoteSocketAddress().toString().replace("/", "");
    }

    /**
     * returns the name of the device (computer name)
     */
    public static @Nullable String getDeviceName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOGGER.log(Level.SEVERE, "Utils.getDeviceName: error getting device name" + String.format(" [%s]", e.getMessage()) + "\n");
            return null;
        }
    }

    public static String[] getPhysicalAddress() throws SocketException {
        final String format = "%02X"; // To get 2 char output.
        // DHCP Enabled - InterfaceMetric
        Set<String> macs = new LinkedHashSet<>();

        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            byte mac[] = ni.getHardwareAddress(); // Physical Address (MAC - Medium Access Control)
            if (mac != null) {
                final StringBuilder macAddress = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    macAddress.append(String.format("%s" + format, (i == 0) ? "" : ":", mac[i]));
                    //macAddress.append(String.format(format+"%s", mac[i], (i < mac.length - 1) ? ":" : ""));
                }
//                System.out.println(macAddress.toString());
                macs.add(macAddress.toString());
            }
        }
        return macs.toArray(new String[0]);
    }
}

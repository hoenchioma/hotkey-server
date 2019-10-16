package com.rfw.hotkey_server.net;

import com.rfw.hotkey_server.control.*;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for handling JSON packets received from client
 */
public class PacketHandler {
    private static final Logger LOGGER = Logger.getLogger(PacketHandler.class.getName());

    public PacketHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    private ConnectionHandler connectionHandler;

    private KeyboardController keyboardController = new KeyboardController();
    private MouseController mouseController = new MouseController();
    private PowerPointController powerPointController = new PowerPointController();
    private PDFController pdfController = new PDFController();
    private LiveScreenController liveScreenController = new LiveScreenController();
    private MacroController macroController = new MacroController();

    public void handle(JSONObject packet) {
        String packetType = packet.getString("type");
        switch (packetType) {
            case "keyboard":
                keyboardController.handleIncomingPacket(packet);
                break;
            case "mouse":
                mouseController.handleIncomingPacket(packet);
                break;
            case "ppt":
                powerPointController.handleIncomingPacket(packet);
                break;
            case "pdf":
                pdfController.handleIncomingPacket(packet);
                break;
            case "liveScreen":
                liveScreenController.handleIncomingPacket(packet);
                break;
            case "macro":
                macroController.handleIncomingPacket(packet);
                break;
            default:
                LOGGER.log(Level.SEVERE, "PacketHandler.handle: unknown packet type " + packetType);
        }
    }

    public void handle(String message) {
//        LOGGER.log(Level.INFO, "PacketHandler.handle: " + message);
        handle(new JSONObject(new JSONTokener(message)));
    }

    public void exit() {
        liveScreenController.stop();
    }
}

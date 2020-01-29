package com.rfw.hotkey_server.net;

import com.rfw.hotkey_server.control.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for handling JSON packets received from client
 *
 * @author Raheeb Hassan
 */
public class PacketHandler {
    private static final Logger LOGGER = Logger.getLogger(PacketHandler.class.getName());

    public PacketHandler(ConnectionHandler connectionHandler) throws AWTException {
        this.connectionHandler = connectionHandler;
    }

    private ConnectionHandler connectionHandler;

    private BaseKeyboardController keyboard = new BaseKeyboardController();
    private KeyboardController keyboardController = new KeyboardController(keyboard);
    private MouseController mouseController = new MouseController();
    private PowerPointController powerPointController = new PowerPointController();
    private PDFController pdfController = new PDFController();
    private LiveScreenController liveScreenController = new LiveScreenController();
    private MediaController mediaController = new MediaController();
    private MacroController macroController = new MacroController(keyboard);

    public void handle(JSONObject packet) {
        String packetType = packet.getString("type");
        switch (packetType) {
            case "keyboard":    keyboardController.handle(packet);    break;
            case "mouse":       mouseController.handle(packet);       break;
            case "ppt":         powerPointController.handle(packet);  break;
            case "pdf":         pdfController.handle(packet);         break;
            case "liveScreen":  liveScreenController.handle(packet);  break;
            case "media":       mediaController.handle(packet);       break;
            case "macro":       macroController.handle(packet);       break;
            case "ping":        handlePing(packet);                                 break;
            default:
                LOGGER.log(Level.SEVERE, "PacketHandler.handle: unknown packet type " + packetType + "\n");
        }
    }

    public void handle(String message) {
//        LOGGER.log(Level.INFO, "PacketHandler.handle: " + message);
        handle(new JSONObject(new JSONTokener(message)));
    }

    private void handlePing(JSONObject packet) {
        try {
            if (packet.getBoolean("pingBack")) {
                connectionHandler.sendPacket(new JSONObject().put("type", "ping"));
            }
        } catch (JSONException ignored) {}
    }

    public void cleanUp() {
        keyboard.cleanUp();
        liveScreenController.cleanUp();
        powerPointController.cleanUp();
    }
}

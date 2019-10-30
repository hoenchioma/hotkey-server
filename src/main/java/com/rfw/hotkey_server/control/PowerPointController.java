package com.rfw.hotkey_server.control;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PowerPointController {
    private static final Logger LOGGER = Logger.getLogger(PowerPointController.class.getName());
    private Robot robot;

    public PowerPointController() {
        try {
            robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error Occurred!");
        }
    }
    public void keyPress(int keyCode) {
        robot.keyPress(keyCode);
    }

    public void keyRelease(int keyCode) {
        robot.keyRelease(keyCode);
    }

    public void type(int keyCode) {
        keyPress(keyCode);
        robot.delay(50);
        keyRelease(keyCode);
    }

    public void pressModifierButton(String keyword) {
        switch (keyword) {
            case "beginning" :
                type(KeyEvent.VK_F5);
                break;
            case "current" :
                keyPress(KeyEvent.VK_SHIFT);
                keyPress(KeyEvent.VK_F5);
                robot.delay(10);
                keyRelease(KeyEvent.VK_F5);
                keyRelease(KeyEvent.VK_SHIFT);
                break;
            case "ESC" :
                type(KeyEvent.VK_ESCAPE);
                break;
            case "UP":
                type(KeyEvent.VK_UP);
                break;
            case "DOWN":
                type(KeyEvent.VK_DOWN);
                break;
            case "LEFT":
                type(KeyEvent.VK_LEFT);
                break;
            case "RIGHT":
                type(KeyEvent.VK_RIGHT);
                break;
        }
    }
    public void handleIncomingPacket(JSONObject packet) {
        String action = packet.getString("action");
        switch (action) {

            case "modifier" :
                pressModifierButton(packet.getString("key"));

                // TODO: (Wadith) implement other actions
            default:
                LOGGER.log(Level.SEVERE, "PowerPointController.handleIncomingPacket: invalid powerpoint action\n");
        }
    }
}

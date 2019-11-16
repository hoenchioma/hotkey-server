package com.rfw.hotkey_server.control;

import com.rfw.hotkey_server.ui.PPTPointer;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PowerPointController {
    private static final Logger LOGGER = Logger.getLogger(PowerPointController.class.getName());
    private Robot robot;
    private PPTPointer pptPointer;
    private int pointerX,pointerY;
    public PowerPointController() {
        try {
            robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error Occurred!");
        }

        pointerX = 400;
        pointerY = 400;

        pptPointer = new PPTPointer();
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
                break;
            case "pointer" :
                pointerX += Integer.parseInt(packet.getString("deltaX"));
                pointerY += Integer.parseInt(packet.getString("deltaY"));
                pptPointer.movePointer(pointerX,pointerY);
                // TODO: (Wadith) implement other actions
            default:
                LOGGER.log(Level.SEVERE, "PowerPointController.handleIncomingPacket: invalid powerpoint action\n");
        }
    }
}

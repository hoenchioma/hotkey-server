package com.rfw.hotkey_server.util;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyboardController {
    private static final Logger LOGGER = Logger.getLogger(KeyboardController.class.getName());

    private Robot robot;

    public KeyboardController() {
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

    public void ctrlS() {
        keyPress(KeyEvent.VK_CONTROL);
        keyPress(KeyEvent.VK_S);
        robot.delay(10);
        keyRelease(KeyEvent.VK_S);
        keyRelease(KeyEvent.VK_CONTROL);
    }

    public void ctrlV() {
        keyPress(KeyEvent.VK_CONTROL);
        keyPress(KeyEvent.VK_V);
        robot.delay(10);
        keyRelease(KeyEvent.VK_V);
        keyRelease(KeyEvent.VK_CONTROL);
    }

    public void ctrlAltT() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_T);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_T);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void ctrlAltL() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_L);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_L);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void ctrlShiftZ() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_Z);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_Z);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void altF4() {
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_F4);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_F4);
        robot.keyRelease(KeyEvent.VK_ALT);
    }

    public void type(int keyCode) {
        keyPress(keyCode);
        robot.delay(10);
        keyRelease(keyCode);
    }


    /**
     * There will be Three types of keyWord
     * 1. TYPE_MODIFIER
     * 2. TYPE_CHARACTER
     * 3. TYPE_COMMAND
     */
    public void pressCommandButton(String keyword) {
        switch (keyword) {
            case "COPY":
                keyPress(KeyEvent.VK_CONTROL);
                keyPress(KeyEvent.VK_S);
                robot.delay(10);
                keyRelease(KeyEvent.VK_S);
                keyRelease(KeyEvent.VK_CONTROL);
                break;
            case "PASTE":
                keyPress(KeyEvent.VK_CONTROL);
                keyPress(KeyEvent.VK_V);
                robot.delay(10);
                keyRelease(KeyEvent.VK_V);
                keyRelease(KeyEvent.VK_CONTROL);
                break;
        }
    }

    public void pressModifierButton(String keyword) {
        switch (keyword) {
            case "ESC":
                type(KeyEvent.VK_ESCAPE);
                break;
            case "HOME":
                type(KeyEvent.VK_HOME);
                break;
            case "TAB":
                type(KeyEvent.VK_TAB);
                break;
            case "PGUP":
                type(KeyEvent.VK_PAGE_UP);
                break;
            case "PGDN":
                type(KeyEvent.VK_PAGE_DOWN);
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

    public void pressAndHoldButton(String keyword) {
        switch (keyword) {
            case "SHIFT":
                keyPress(KeyEvent.VK_SHIFT);
                robot.delay(10);
                keyRelease(KeyEvent.VK_SHIFT);
                break;
        }
    }

    public void pressCharacterButton(String keyword) {
        char c = Character.toUpperCase(keyword.charAt(0));
        try {
            int keyCode;
            if (c == ' ') keyCode = KeyEvent.VK_SPACE;
            else if (c == '\n') keyCode = KeyEvent.VK_ENTER;
            else if (c == '\b') keyCode = KeyEvent.VK_BACK_SPACE;
                // use reflection to get the keycode
            else keyCode = KeyEvent.class.getField("VK_" + c).getInt(null);
            type(keyCode);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "KeyboardController.pressCharacterButton: KeyEvent class reflection error");
            e.printStackTrace();
        }
    }
 
    public void handleIncomingPacket(JSONObject packet) {
        String action = packet.getString("action");
        switch (action) {
            case "char":
                pressCharacterButton(packet.getString("key"));
                break;
            // TODO: (Wadith) implement other actions
            default:
                LOGGER.log(Level.SEVERE, "KeyboardController.handleIncomingPacket: invalid keyboard action");
        }
    }

}

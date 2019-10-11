package com.rfw.hotkey_server.util;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.awt.event.KeyEvent.*;

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
        robot.delay(50);
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
        char c = keyword.charAt(0);
        try {
            typeCharacter(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doType(int... keyCodes) {
        int length = keyCodes.length;
        for (int i = 0; i < length; i++) {
            robot.keyPress(keyCodes[i]);
        }
        robot.delay(10);
        for (int i = length - 1; i >= 0; i--) {
            robot.keyRelease(keyCodes[i]);
        }
        robot.keyRelease(VK_SHIFT);
    }

    public void typeCharacter(char character) {
        switch (character) {
            case 'a': doType(VK_A); break;
            case 'b': doType(VK_B); break;
            case 'c': doType(VK_C); break;
            case 'd': doType(VK_D); break;
            case 'e': doType(VK_E); break;
            case 'f': doType(VK_F); break;
            case 'g': doType(VK_G); break;
            case 'h': doType(VK_H); break;
            case 'i': doType(VK_I); break;
            case 'j': doType(VK_J); break;
            case 'k': doType(VK_K); break;
            case 'l': doType(VK_L); break;
            case 'm': doType(VK_M); break;
            case 'n': doType(VK_N); break;
            case 'o': doType(VK_O); break;
            case 'p': doType(VK_P); break;
            case 'q': doType(VK_Q); break;
            case 'r': doType(VK_R); break;
            case 's': doType(VK_S); break;
            case 't': doType(VK_T); break;
            case 'u': doType(VK_U); break;
            case 'v': doType(VK_V); break;
            case 'w': doType(VK_W); break;
            case 'x': doType(VK_X); break;
            case 'y': doType(VK_Y); break;
            case 'z': doType(VK_Z); break;
            case 'A': doType(VK_SHIFT, VK_A); break;
            case 'B': doType(VK_SHIFT, VK_B); break;
            case 'C': doType(VK_SHIFT, VK_C); break;
            case 'D': doType(VK_SHIFT, VK_D); break;
            case 'E': doType(VK_SHIFT, VK_E); break;
            case 'F': doType(VK_SHIFT, VK_F); break;
            case 'G': doType(VK_SHIFT, VK_G); break;
            case 'H': doType(VK_SHIFT, VK_H); break;
            case 'I': doType(VK_SHIFT, VK_I); break;
            case 'J': doType(VK_SHIFT, VK_J); break;
            case 'K': doType(VK_SHIFT, VK_K); break;
            case 'L': doType(VK_SHIFT, VK_L); break;
            case 'M': doType(VK_SHIFT, VK_M); break;
            case 'N': doType(VK_SHIFT, VK_N); break;
            case 'O': doType(VK_SHIFT, VK_O); break;
            case 'P': doType(VK_SHIFT, VK_P); break;
            case 'Q': doType(VK_SHIFT, VK_Q); break;
            case 'R': doType(VK_SHIFT, VK_R); break;
            case 'S': doType(VK_SHIFT, VK_S); break;
            case 'T': doType(VK_SHIFT, VK_T); break;
            case 'U': doType(VK_SHIFT, VK_U); break;
            case 'V': doType(VK_SHIFT, VK_V); break;
            case 'W': doType(VK_SHIFT, VK_W); break;
            case 'X': doType(VK_SHIFT, VK_X); break;
            case 'Y': doType(VK_SHIFT, VK_Y); break;
            case 'Z': doType(VK_SHIFT, VK_Z); break;
            case '`': doType(VK_BACK_QUOTE); break;
            case '0': doType(VK_0); break;
            case '1': doType(VK_1); break;
            case '2': doType(VK_2); break;
            case '3': doType(VK_3); break;
            case '4': doType(VK_4); break;
            case '5': doType(VK_5); break;
            case '6': doType(VK_6); break;
            case '7': doType(VK_7); break;
            case '8': doType(VK_8); break;
            case '9': doType(VK_9); break;
            case '-': doType(VK_MINUS); break;
            case '=': doType(VK_EQUALS); break;
            case '~': doType(VK_BACK_QUOTE); break;
            case '!': doType(VK_SHIFT, VK_1); break;
            case '@': doType(VK_SHIFT, VK_2); break;
            case '#': doType(VK_SHIFT, VK_3); break;
            case '$': doType(VK_SHIFT, VK_4); break;
            case '%': doType(VK_SHIFT, VK_5); break;
            case '^': doType(VK_SHIFT, VK_6); break;
            case '&': doType(VK_SHIFT, VK_7); break;
            case '*': doType(VK_SHIFT, VK_8); break;
            case '(': doType(VK_SHIFT, VK_9); break;
            case ')': doType(VK_SHIFT, VK_0); break;
            case '_': doType(VK_SHIFT, VK_MINUS); break;
            case '+': doType(VK_SHIFT, VK_EQUALS); break;
            case '\t': doType(VK_TAB); break;
            case '\n': doType(VK_ENTER); break;
            case '[': doType(VK_OPEN_BRACKET); break;
            case ']': doType(VK_CLOSE_BRACKET); break;
            case '\\': doType(VK_BACK_SLASH); break;
            case '{': doType(VK_SHIFT, VK_OPEN_BRACKET); break;
            case '}': doType(VK_SHIFT, VK_CLOSE_BRACKET); break;
            case '|': doType(VK_SHIFT, VK_BACK_SLASH); break;
            case ';': doType(VK_SEMICOLON); break;
            case ':': doType(VK_SHIFT, VK_SEMICOLON); break;
            case '\'': doType(VK_QUOTE); break;
            case '"': doType(VK_SHIFT, VK_QUOTE); break;
            case ',': doType(VK_COMMA); break;
            case '<': doType(VK_SHIFT, VK_COMMA); break;
            case '.': doType(VK_PERIOD); break;
            case '>': doType(VK_SHIFT, VK_PERIOD); break;
            case '/': doType(VK_SLASH); break;
            case '?': doType(VK_SHIFT, VK_SLASH); break;
            case ' ': doType(VK_SPACE); break;
            case '\b': doType(VK_BACK_SPACE); break;
            default: robot.keyRelease(VK_SHIFT);
                //throw new IllegalArgumentException("Cannot type character " + character);
        }
    }

    public void handleIncomingPacket(JSONObject packet) {
        String action = packet.getString("action");
        switch (action) {
            case "char":
                pressCharacterButton(packet.getString("key"));
                break;
            case "modifier":
                pressModifierButton(packet.getString("key"));
                break;
            case "command":
                pressCommandButton(packet.getString("key"));
                break;
            default:
                LOGGER.log(Level.SEVERE, "KeyboardController.handleIncomingPacket: invalid keyboard action");
        }
    }

}

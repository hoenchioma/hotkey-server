package com.rfw.hotkey_server.control;

import org.json.JSONObject;

import java.lang.reflect.*;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for handling and executing macro key combinations.
 *
 * @author Farhan Kabir
 */

public class MacroController {
    private static final Logger LOGGER = Logger.getLogger(MacroController.class.getName());

    private Robot robot;

    public MacroController() throws AWTException {
        robot = new Robot();
    }

    public void handleIncomingPacket(JSONObject packet) {
        pressKey(packet, 0);
    }

    public void pressKey(JSONObject packet, int currentKey) {
        int totalKeys = packet.getInt("size");

        if (currentKey < totalKeys) {
            switch (packet.getString(Integer.toString(currentKey))) {
                case "A":
                    robot.keyPress(KeyEvent.VK_A);
                    break;
                case "B":
                    robot.keyPress(KeyEvent.VK_B);
                    break;
                case "C":
                    robot.keyPress(KeyEvent.VK_C);
                    break;
                case "D":
                    robot.keyPress(KeyEvent.VK_D);
                    break;
                case "E":
                    robot.keyPress(KeyEvent.VK_E);
                    break;
                case "F":
                    robot.keyPress(KeyEvent.VK_F);
                    break;
                case "G":
                    robot.keyPress(KeyEvent.VK_G);
                    break;
                case "H":
                    robot.keyPress(KeyEvent.VK_H);
                    break;
                case "I":
                    robot.keyPress(KeyEvent.VK_I);
                    break;
                case "J":
                    robot.keyPress(KeyEvent.VK_J);
                    break;
                case "K":
                    robot.keyPress(KeyEvent.VK_K);
                    break;
                case "L":
                    robot.keyPress(KeyEvent.VK_L);
                    break;
                case "M":
                    robot.keyPress(KeyEvent.VK_M);
                    break;
                case "N":
                    robot.keyPress(KeyEvent.VK_N);
                    break;
                case "O":
                    robot.keyPress(KeyEvent.VK_O);
                    break;
                case "P":
                    robot.keyPress(KeyEvent.VK_P);
                    break;
                case "Q":
                    robot.keyPress(KeyEvent.VK_Q);
                    break;
                case "R":
                    robot.keyPress(KeyEvent.VK_R);
                    break;
                case "S":
                    robot.keyPress(KeyEvent.VK_S);
                    break;
                case "T":
                    robot.keyPress(KeyEvent.VK_T);
                    break;
                case "U":
                    robot.keyPress(KeyEvent.VK_U);
                    break;
                case "V":
                    robot.keyPress(KeyEvent.VK_V);
                    break;
                case "W":
                    robot.keyPress(KeyEvent.VK_W);
                    break;
                case "X":
                    robot.keyPress(KeyEvent.VK_X);
                    break;
                case "Y":
                    robot.keyPress(KeyEvent.VK_Y);
                    break;
                case "Z":
                    robot.keyPress(KeyEvent.VK_Z);
                    break;
                case "0":
                    robot.keyPress(KeyEvent.VK_0);
                    break;
                case "1":
                    robot.keyPress(KeyEvent.VK_1);
                    break;
                case "2":
                    robot.keyPress(KeyEvent.VK_2);
                    break;
                case "3":
                    robot.keyPress(KeyEvent.VK_3);
                    break;
                case "4":
                    robot.keyPress(KeyEvent.VK_4);
                    break;
                case "5":
                    robot.keyPress(KeyEvent.VK_5);
                    break;
                case "6":
                    robot.keyPress(KeyEvent.VK_6);
                    break;
                case "7":
                    robot.keyPress(KeyEvent.VK_7);
                    break;
                case "8":
                    robot.keyPress(KeyEvent.VK_8);
                    break;
                case "9":
                    robot.keyPress(KeyEvent.VK_9);
                    break;
                case "ESC":
                    robot.keyPress(KeyEvent.VK_ESCAPE);
                    break;
                case "END":
                    robot.keyPress(KeyEvent.VK_END);
                    break;
                case "CTRL":
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    break;
                case "ALT":
                    robot.keyPress(KeyEvent.VK_ALT);
                    break;
                case "SHIFT":
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    break;
                case "INS":
                    robot.keyPress(KeyEvent.VK_INSERT);
                    break;
                case "DEL":
                    robot.keyPress(KeyEvent.VK_DELETE);
                    break;
                case "HOME":
                    robot.keyPress(KeyEvent.VK_HOME);
                    break;
                case "PGUP":
                    robot.keyPress(KeyEvent.VK_PAGE_UP);
                    break;
                case "UP":
                    robot.keyPress(KeyEvent.VK_UP);
                    break;
                case "DOWN":
                    robot.keyPress(KeyEvent.VK_DOWN);
                    break;
                case "LEFT":
                    robot.keyPress(KeyEvent.VK_LEFT);
                    break;
                case "RIGHT":
                    robot.keyPress(KeyEvent.VK_RIGHT);
                    break;
            }
            pressKey(packet, currentKey + 1);
            switch (packet.getString(Integer.toString(currentKey))) {
                case "A":
                    robot.keyRelease(KeyEvent.VK_A);
                    break;
                case "B":
                    robot.keyRelease(KeyEvent.VK_B);
                    break;
                case "C":
                    robot.keyRelease(KeyEvent.VK_C);
                    break;
                case "D":
                    robot.keyRelease(KeyEvent.VK_D);
                    break;
                case "E":
                    robot.keyRelease(KeyEvent.VK_E);
                    break;
                case "F":
                    robot.keyRelease(KeyEvent.VK_F);
                    break;
                case "G":
                    robot.keyRelease(KeyEvent.VK_G);
                    break;
                case "H":
                    robot.keyRelease(KeyEvent.VK_H);
                    break;
                case "I":
                    robot.keyRelease(KeyEvent.VK_I);
                    break;
                case "J":
                    robot.keyRelease(KeyEvent.VK_J);
                    break;
                case "K":
                    robot.keyRelease(KeyEvent.VK_K);
                    break;
                case "L":
                    robot.keyRelease(KeyEvent.VK_L);
                    break;
                case "M":
                    robot.keyRelease(KeyEvent.VK_M);
                    break;
                case "N":
                    robot.keyRelease(KeyEvent.VK_N);
                    break;
                case "O":
                    robot.keyRelease(KeyEvent.VK_O);
                    break;
                case "P":
                    robot.keyRelease(KeyEvent.VK_P);
                    break;
                case "Q":
                    robot.keyRelease(KeyEvent.VK_Q);
                    break;
                case "R":
                    robot.keyRelease(KeyEvent.VK_R);
                    break;
                case "S":
                    robot.keyRelease(KeyEvent.VK_S);
                    break;
                case "T":
                    robot.keyRelease(KeyEvent.VK_T);
                    break;
                case "U":
                    robot.keyRelease(KeyEvent.VK_U);
                    break;
                case "V":
                    robot.keyRelease(KeyEvent.VK_V);
                    break;
                case "W":
                    robot.keyRelease(KeyEvent.VK_W);
                    break;
                case "X":
                    robot.keyRelease(KeyEvent.VK_X);
                    break;
                case "Y":
                    robot.keyRelease(KeyEvent.VK_Y);
                    break;
                case "Z":
                    robot.keyRelease(KeyEvent.VK_Z);
                    break;
                case "0":
                    robot.keyRelease(KeyEvent.VK_0);
                    break;
                case "1":
                    robot.keyRelease(KeyEvent.VK_1);
                    break;
                case "2":
                    robot.keyRelease(KeyEvent.VK_2);
                    break;
                case "3":
                    robot.keyRelease(KeyEvent.VK_3);
                    break;
                case "4":
                    robot.keyRelease(KeyEvent.VK_4);
                    break;
                case "5":
                    robot.keyRelease(KeyEvent.VK_5);
                    break;
                case "6":
                    robot.keyRelease(KeyEvent.VK_6);
                    break;
                case "7":
                    robot.keyRelease(KeyEvent.VK_7);
                    break;
                case "8":
                    robot.keyRelease(KeyEvent.VK_8);
                    break;
                case "9":
                    robot.keyRelease(KeyEvent.VK_9);
                    break;
                case "ESC":
                    robot.keyRelease(KeyEvent.VK_ESCAPE);
                    break;
                case "END":
                    robot.keyRelease(KeyEvent.VK_END);
                    break;
                case "CTRL":
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    break;
                case "ALT":
                    robot.keyRelease(KeyEvent.VK_ALT);
                    break;
                case "SHIFT":
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                    break;
                case "INS":
                    robot.keyRelease(KeyEvent.VK_INSERT);
                    break;
                case "DEL":
                    robot.keyRelease(KeyEvent.VK_DELETE);
                    break;
                case "HOME":
                    robot.keyRelease(KeyEvent.VK_HOME);
                    break;
                case "PGUP":
                    robot.keyRelease(KeyEvent.VK_PAGE_UP);
                    break;
                case "PGDN":
                    robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
                    break;
                case "UP":
                    robot.keyRelease(KeyEvent.VK_UP);
                    break;
                case "DOWN":
                    robot.keyRelease(KeyEvent.VK_DOWN);
                    break;
                case "LEFT":
                    robot.keyRelease(KeyEvent.VK_LEFT);
                    break;
                case "RIGHT":
                    robot.keyRelease(KeyEvent.VK_RIGHT);
                    break;
            }


        }
    }
}

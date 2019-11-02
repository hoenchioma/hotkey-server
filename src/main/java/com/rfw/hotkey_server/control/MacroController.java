package com.rfw.hotkey_server.control;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MacroController
{
    private static final Logger LOGGER = Logger.getLogger(MacroController.class.getName());

    private Robot robot;

    public MacroController() {
        try {
            robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error Occurred!");
        }
    }

    public void handleIncomingPacket(JSONObject packet) {

    }

    public void pressKey(JSONObject packet, int currentKey){
        int totalKeys = packet.getInt("size");
        if(currentKey < totalKeys){
        switch (packet.getString(Integer.toString(currentKey))) {
            case "a":
                robot.keyPress(KeyEvent.VK_A);
                break;
            case "b":
                robot.keyPress(KeyEvent.VK_B);
                break;
            case "c":
                robot.keyPress(KeyEvent.VK_C);
                break;
            case "d":
                robot.keyPress(KeyEvent.VK_D);
                break;
            case "e":
                robot.keyPress(KeyEvent.VK_E);
                break;
            case "f":
                robot.keyPress(KeyEvent.VK_F);
                break;
            case "g":
                robot.keyPress(KeyEvent.VK_G);
                break;
            case "h":
                robot.keyPress(KeyEvent.VK_H);
                break;
            case "i":
                robot.keyPress(KeyEvent.VK_I);
                break;
            case "j":
                robot.keyPress(KeyEvent.VK_J);
                break;
            case "k":
                robot.keyPress(KeyEvent.VK_K);
                break;
            case "l":
                robot.keyPress(KeyEvent.VK_L);
                break;
            case "m":
                robot.keyPress(KeyEvent.VK_M);
                break;
            case "n":
                robot.keyPress(KeyEvent.VK_N);
                break;
            case "o":
                robot.keyPress(KeyEvent.VK_O);
                break;
            case "p":
                robot.keyPress(KeyEvent.VK_P);
                break;
            case "q":
                robot.keyPress(KeyEvent.VK_Q);
                break;
            case "r":
                robot.keyPress(KeyEvent.VK_R);
                break;
            case "s":
                robot.keyPress(KeyEvent.VK_S);
                break;
            case "t":
                robot.keyPress(KeyEvent.VK_T);
                break;
            case "u":
                robot.keyPress(KeyEvent.VK_U);
                break;
            case "v":
                robot.keyPress(KeyEvent.VK_V);
                break;
            case "w":
                robot.keyPress(KeyEvent.VK_W);
                break;
            case "x":
                robot.keyPress(KeyEvent.VK_X);
                break;
            case "y":
                robot.keyPress(KeyEvent.VK_Y);
                break;
            case "z":
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
            case "PGDN":
                robot.keyPress(KeyEvent.VK_PAGE_DOWN);
                break;
        }
            pressKey(packet, currentKey + 1);
        switch (packet.getString(Integer.toString(currentKey))) {
            case "a":
                robot.keyRelease(KeyEvent.VK_A);
                break;
            case "b":
                robot.keyRelease(KeyEvent.VK_B);
                break;
            case "c":
                robot.keyRelease(KeyEvent.VK_C);
                break;
            case "d":
                robot.keyRelease(KeyEvent.VK_D);
                break;
            case "e":
                robot.keyRelease(KeyEvent.VK_E);
                break;
            case "f":
                robot.keyRelease(KeyEvent.VK_F);
                break;
            case "g":
                robot.keyRelease(KeyEvent.VK_G);
                break;
            case "h":
                robot.keyRelease(KeyEvent.VK_H);
                break;
            case "i":
                robot.keyRelease(KeyEvent.VK_I);
                break;
            case "j":
                robot.keyRelease(KeyEvent.VK_J);
                break;
            case "k":
                robot.keyRelease(KeyEvent.VK_K);
                break;
            case "l":
                robot.keyRelease(KeyEvent.VK_L);
                break;
            case "m":
                robot.keyRelease(KeyEvent.VK_M);
                break;
            case "n":
                robot.keyRelease(KeyEvent.VK_N);
                break;
            case "o":
                robot.keyRelease(KeyEvent.VK_O);
                break;
            case "p":
                robot.keyRelease(KeyEvent.VK_P);
                break;
            case "q":
                robot.keyRelease(KeyEvent.VK_Q);
                break;
            case "r":
                robot.keyRelease(KeyEvent.VK_R);
                break;
            case "s":
                robot.keyRelease(KeyEvent.VK_S);
                break;
            case "t":
                robot.keyRelease(KeyEvent.VK_T);
                break;
            case "u":
                robot.keyRelease(KeyEvent.VK_U);
                break;
            case "v":
                robot.keyRelease(KeyEvent.VK_V);
                break;
            case "w":
                robot.keyRelease(KeyEvent.VK_W);
                break;
            case "x":
                robot.keyRelease(KeyEvent.VK_X);
                break;
            case "y":
                robot.keyRelease(KeyEvent.VK_Y);
                break;
            case "z":
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
        }


        }
    }
}

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

        pressKey(packet,0);
        // ArrayList<String> keys = new ArrayList<>();
        /*int totalKeys = packet.getInt("size");
        for(int i = 0; i < totalKeys; i++) {
            switch (packet.getString(Integer.toString(i))) {
                case "A":
                    keys.add("VK_A");
                    break;
                case "B":
                    keys.add("VK_B");
                    break;
                case "C":
                    keys.add("VK_C");
                    break;
                case "D":
                    keys.add("VK_D");
                    break;
                case "E":
                    keys.add("VK_E");
                    break;
                case "F":
                    keys.add("VK_F");
                    break;
                case "G":
                    keys.add("VK_G");
                    break;
                case "H":
                    keys.add("VK_H");
                    break;
                case "I":
                    keys.add("VK_I");
                    break;
                case "J":
                    keys.add("VK_J");
                    break;
                case "K":
                    keys.add("VK_K");
                    break;
                case "L":
                    keys.add("VK_L");
                    break;
                case "M":
                    keys.add("VK_M");
                    break;
                case "N":
                    keys.add("VK_N");
                    break;
                case "O":
                    keys.add("VK_O");
                    break;
                case "P":
                    keys.add("VK_P");
                    break;
                case "Q":
                    keys.add("VK_Q");
                    break;
                case "R":
                    keys.add("VK_R");
                    break;
                case "S":
                    keys.add("VK_S");
                    break;
                case "T":
                    keys.add("VK_T");
                    break;
                case "U":
                    keys.add("VK_U");
                    break;
                case "V":
                    keys.add("VK_V");
                    break;
                case "W":
                    keys.add("VK_W");
                    break;
                case "X":
                    keys.add("VK_X");
                    break;
                case "Y":
                    keys.add("VK_Y");
                    break;
                case "Z":
                    keys.add("VK_Z");
                    break;
                case "0":
                    keys.add("VK_0");
                    break;
                case "1":
                    keys.add("VK_1");
                    break;
                case "2":
                    keys.add("VK_2");
                    break;
                case "3":
                    keys.add("VK_3");
                    break;
                case "4":
                    keys.add("VK_4");
                    break;
                case "5":
                    keys.add("VK_5");
                    break;
                case "6":
                    keys.add("VK_6");
                    break;
                case "7":
                    keys.add("VK_7");
                    break;
                case "8":
                    keys.add("VK_8");
                    break;
                case "9":
                    keys.add("VK_9");
                    break;
                case "ESC":
                    keys.add("VK_ESCAPE");
                    break;
                case "END":
                    keys.add("VK_END");
                    break;
                case "CTRL":
                    keys.add("VK_CONTROL");
                    break;
                case "ALT":
                    keys.add("VK_ALT");
                    break;
                case "SHIFT":
                    keys.add("VK_SHIFT");
                    break;
                case "INS":
                    keys.add("VK_INSERT");
                    break;
                case "DEL":
                    keys.add("VK_DELETE");
                    break;
                case "HOME":
                    keys.add("VK_HOME");
                    break;
                case "PGUP":
                    keys.add("VK_PAGE_UP");
                    break;
                case "PGDN":
                    keys.add("VK_PAGE_DOWN");
                    break;
            }

        }
        sendKeysCombo(keys);*/

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
                case "PGDN":
                    robot.keyPress(KeyEvent.VK_PAGE_DOWN);
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
            }


        }
    }
    /*public static void sendKeysCombo(ArrayList keys) {
        try {

            Robot robot = new Robot();

            Class<?> cl = KeyEvent.class;

            int [] intKeys = new int [keys.size()];

            for (int i = 0; i < keys.size(); i++) {
                Field field = cl.getDeclaredField(keys.get(i).toString());
                intKeys[i] = field.getInt(field);
                robot.keyPress(intKeys[i]);
            }

            for (int i = keys.size() - 1; i >= 0; i--)
                robot.keyRelease(intKeys[i]);
        }
        catch (Throwable e) {
            System.err.println(e);
        }
    }*/
}

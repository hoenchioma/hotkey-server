package com.rfw.hotkey_server.util;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDFController {

    private static final Logger LOGGER = Logger.getLogger(PowerPointController.class.getName());
    private Robot robot;
    PDFController(){
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
            case "fit_w" :
                keyPress(KeyEvent.VK_CONTROL);
                keyPress(KeyEvent.VK_2);
                robot.delay(10);
                keyRelease(KeyEvent.VK_2);
                keyRelease(KeyEvent.VK_CONTROL);

                break;
            case "fit_h" :
                keyPress(KeyEvent.VK_CONTROL);
                keyPress(KeyEvent.VK_1);
                robot.delay(10);
                keyRelease(KeyEvent.VK_1);
                keyRelease(KeyEvent.VK_CONTROL);

                break;
            case "zoom_in" :
                keyPress(KeyEvent.VK_CONTROL);
                keyPress(KeyEvent.VK_EQUALS);
                robot.delay(10);
                keyRelease(KeyEvent.VK_EQUALS);
                keyRelease(KeyEvent.VK_CONTROL);

                break;
            case "zoom_out" :
                keyPress(KeyEvent.VK_CONTROL);
                keyPress(KeyEvent.VK_MINUS);
                robot.delay(10);
                keyRelease(KeyEvent.VK_MINUS);
                keyRelease(KeyEvent.VK_CONTROL);

                break;
            case "fullscreen" :
                keyPress(KeyEvent.VK_CONTROL);
                keyPress(KeyEvent.VK_L);
                robot.delay(10);
                keyRelease(KeyEvent.VK_L);
                keyRelease(KeyEvent.VK_CONTROL);

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
    public void gotoPage(String number){
        keyPress(KeyEvent.VK_SHIFT);
        keyPress(KeyEvent.VK_CONTROL);
        keyPress(KeyEvent.VK_N);
        robot.delay(20);
        keyRelease(KeyEvent.VK_SHIFT);
        keyRelease(KeyEvent.VK_CONTROL);
        keyRelease(KeyEvent.VK_N);

        for(int i=0;i<number.length();i++){
            keyPress((int)number.charAt(i));
            robot.delay(10);
            keyRelease((int)number.charAt(i));
        }
        keyPress(KeyEvent.VK_ENTER);
        robot.delay(30);
        keyRelease(KeyEvent.VK_ENTER);
    }
    public void handleIncomingPacket(JSONObject packet) {
        String action = packet.getString("action");
        switch (action) {

            case "modifier" :
                pressModifierButton(packet.getString("key"));
                break;
            case "page":
                gotoPage(packet.getString("key"));
                break;
                // TODO: (Wadith) implement other actions
            default:
                LOGGER.log(Level.SEVERE, "PowerPointController.handleIncomingPacket: invalid powerpoint action");
        }
    }
}

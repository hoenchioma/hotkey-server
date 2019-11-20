package com.rfw.hotkey_server.control;

import org.json.JSONObject;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shadman Wadith
 */
public class PDFController {
    private static final Logger LOGGER = Logger.getLogger(PowerPointController.class.getName());

    private Robot robot;

    public PDFController() throws AWTException {
        robot = new Robot();
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

    /**
     * platform == 1 means Adobe Acrobat Reader Command
     * platform == 2 means Evince PDF Reader Command
     */
    public void pressModifierButton(String keyword, String platform) {
        switch (keyword) {
            case "fit_w":
                if (platform.equals("1")) {
                    keyPress(KeyEvent.VK_CONTROL);
                    keyPress(KeyEvent.VK_2);
                    robot.delay(10);
                    keyRelease(KeyEvent.VK_2);
                    keyRelease(KeyEvent.VK_CONTROL);
                } else if (platform.equals("2")) {
                    type(KeyEvent.VK_W);
                }


                break;
            case "fit_h":
                if (platform.equals("1")) {
                    LOGGER.log(Level.SEVERE, "Fit_h");
                    keyPress(KeyEvent.VK_CONTROL);
                    keyPress(KeyEvent.VK_1);
                    robot.delay(10);
                    keyRelease(KeyEvent.VK_1);
                    keyRelease(KeyEvent.VK_CONTROL);
                } else if (platform.equals("2")) {
                    type(KeyEvent.VK_F);
                }
                break;
            case "zoom_in":
                keyPress(KeyEvent.VK_CONTROL);
                keyPress(KeyEvent.VK_EQUALS);
                robot.delay(10);
                keyRelease(KeyEvent.VK_EQUALS);
                keyRelease(KeyEvent.VK_CONTROL);

                break;
            case "zoom_out":
                keyPress(KeyEvent.VK_CONTROL);
                keyPress(KeyEvent.VK_MINUS);
                robot.delay(10);
                keyRelease(KeyEvent.VK_MINUS);
                keyRelease(KeyEvent.VK_CONTROL);

                break;
            case "fullscreen":
                if (platform.equals("1")) {
                    keyPress(KeyEvent.VK_CONTROL);
                    keyPress(KeyEvent.VK_L);
                    robot.delay(10);
                    keyRelease(KeyEvent.VK_L);
                    keyRelease(KeyEvent.VK_CONTROL);
                } else if (platform.equals("2")) {
                    type(KeyEvent.VK_F5);
                }
                break;
            case "ESC":
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

    public void gotoPage(String number, String platform) {

        LOGGER.log(Level.SEVERE, "platform : " + platform);
        if (platform.equals("1")) {

            keyPress(KeyEvent.VK_SHIFT);
            keyPress(KeyEvent.VK_CONTROL);
            keyPress(KeyEvent.VK_N);
            robot.delay(20);
            keyRelease(KeyEvent.VK_SHIFT);
            keyRelease(KeyEvent.VK_CONTROL);
            keyRelease(KeyEvent.VK_N);
        } else if (platform.equals("2")) {
            keyPress(KeyEvent.VK_CONTROL);
            keyPress(KeyEvent.VK_L);
            robot.delay(20);
            keyRelease(KeyEvent.VK_CONTROL);
            keyRelease(KeyEvent.VK_L);

        }
        String checkNumber = " ";
        int pageNumber;
        //LOGGER.log(Level.SEVERE,"From Client : "+number+ "  and Length "+number.length());
        for (int i = 0; i < number.length(); i++) {

            pageNumber = (int) number.charAt(i);
          //  LOGGER.log(Level.SEVERE,"Ascii "+String.valueOf(pageNumber));
            keyPress(pageNumber);
            checkNumber+= number.charAt(i);
            robot.delay(20);
            keyRelease(pageNumber);
        }
       // LOGGER.log(Level.SEVERE,"Goto page : "+checkNumber);
        keyPress(KeyEvent.VK_ENTER);
        robot.delay(20);
        keyRelease(KeyEvent.VK_ENTER);
    }

    public void handleIncomingPacket(JSONObject packet) {
        String action = packet.getString("action");
        String key = packet.getString("key");
        String platform = packet.getString("platform");
        switch (action) {

            case "modifier":
                pressModifierButton(key, platform);
                break;
            case "page":
                gotoPage(key, platform);
                break;
            default:
                LOGGER.log(Level.SEVERE, "PowerPointController.handleIncomingPacket: invalid powerpoint action\n");
        }
    }
}

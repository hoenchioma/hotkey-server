package com.rfw.hotkey_server.control;

import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.awt.event.KeyEvent.*;

/**
 * A class to handle PDF Reader (Adobe Acrobat Reader and Evince Reader)
 *
 * @author Shadman Wadith
 */
public class PDFController {
    private static final Logger LOGGER = Logger.getLogger(PowerPointController.class.getName());

    private static String PLATFORM_ADOBE = "1";
    private static String PLATFORM_EVINCE = "2";

    private BaseKeyboardController keyboard;

    public PDFController(BaseKeyboardController keyboard) {
        this.keyboard = keyboard;
    }

//    public void keyPress(int keyCode) {
//        robot.keyPress(keyCode);
//    }
//
//    public void keyRelease(int keyCode) {
//        robot.keyRelease(keyCode);
//    }
//
//    public void type(int keyCode) {
//        keyPress(keyCode);
//        robot.delay(50);
//        keyRelease(keyCode);
//    }

    /**
     * platform == 1 means Adobe Acrobat Reader Command
     * platform == 2 means Evince PDF Reader Command
     */
    public void pressModifierButton(String keyword, String platform) {
        switch (keyword) {
            case "fitWidth":
                if (platform.equals(PLATFORM_ADOBE)) {
                    keyboard.typeKeys(VK_CONTROL, VK_2);
                } else if (platform.equals(PLATFORM_EVINCE)) {
                    keyboard.typeKey(VK_W);
                }
                break;
            case "fitHeight":
                if (platform.equals(PLATFORM_ADOBE)) {
                    keyboard.typeKeys(VK_CONTROL, VK_1);
                } else if (platform.equals(PLATFORM_EVINCE)) {
                    keyboard.typeKey(VK_F);
                }
                break;
            case "zoomIn":
                keyboard.typeKeys(VK_CONTROL, VK_EQUALS);
                break;
            case "zoomOut":
                keyboard.typeKeys(VK_CONTROL, VK_MINUS);
                break;
            case "fullscreen":
                if (platform.equals(PLATFORM_ADOBE)) {
                    keyboard.typeKeys(VK_CONTROL, VK_L);
                } else if (platform.equals(PLATFORM_EVINCE)) {
                    keyboard.typeKey(VK_F5);
                }
                break;
            case "ESC":
                keyboard.typeKey(VK_ESCAPE);
                break;
            case "UP":
                keyboard.typeKey(VK_UP);
                break;
            case "DOWN":
                keyboard.typeKey(VK_DOWN);
                break;
            case "LEFT":
                keyboard.typeKey(VK_LEFT);
                break;
            case "RIGHT":
                keyboard.typeKey(VK_RIGHT);
                break;
        }
    }

    /**
     *
     * @param number page number of which use wants to go
     * @param platform Adobe Acrobat Reader as 1 or Evince reader as 2
     */
    public void gotoPage(String number, String platform) {
        // show goto page dialog
        if (platform.equals(PLATFORM_ADOBE)) {
            keyboard.typeKeys(VK_SHIFT, VK_CONTROL, VK_N);
        } else if (platform.equals(PLATFORM_EVINCE)) {
            keyboard.typeKeys(VK_CONTROL, VK_L);
        }

        // enter page number
        for (char c: number.toCharArray()) {
            keyboard.typeKey(keyboard.getCharKeyCode(c));
        }

        // press enter
        keyboard.typeKey(VK_ENTER);
    }

    /**
     * Handles incoming packet from client side
     * @param packet JSON Packet
     */
    public void handle(JSONObject packet) {
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
                LOGGER.log(Level.SEVERE, "PowerPointController.handle: invalid powerpoint action\n");
        }
    }
}

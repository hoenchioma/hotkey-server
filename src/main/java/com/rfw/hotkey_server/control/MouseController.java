package com.rfw.hotkey_server.control;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for handling and executing mouse actions.
 *
 * @author Farhan Kabir
 */

public class MouseController {
    private static final Logger LOGGER = Logger.getLogger(MouseController.class.getName());

    private Robot robot;

    public MouseController() throws AWTException {
        robot = new Robot();
    }

    public void handleIncomingPacket(JSONObject packet) {
        String action = packet.getString("action");
        switch (action) {
            case "touchpadMove":
                Point mousePointer = MouseInfo.getPointerInfo().getLocation();
                robot.mouseMove(mousePointer.x + packet.getInt("deltaX"), mousePointer.y + packet.getInt("deltaY"));
                break;
            case "leftClick":
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                break;
            case "rightClick":
                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                break;
            case "scrollMove":
                robot.mouseWheel((int) (packet.getInt("deltaY") / 30));
                break;
            case "scrollClick":
                robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
            default:
                LOGGER.log(Level.SEVERE, "MouseController.handleIncomingPacket: invalid keyboard action\n");
        }
    }
}

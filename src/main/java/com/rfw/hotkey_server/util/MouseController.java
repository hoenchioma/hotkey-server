package com.rfw.hotkey_server.util;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MouseController
{
    private static final Logger LOGGER = Logger.getLogger(MouseController.class.getName());

    private Robot robot;

    public MouseController() {
        try {
            robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error Occurred!");
        }
    }

    public void handleIncomingPacket(JSONObject packet) {
        String action = packet.getString("action");
        switch (action) {
            case "TouchpadMove":
                Point mousePointer = MouseInfo.getPointerInfo().getLocation();
                robot.mouseMove(mousePointer.x + packet.getInt("deltaX"), mousePointer.y + packet.getInt("deltaY"));
                break;
            case "LeftClick" :
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                break;
            case "RightClick" :
                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                break;
            case "ScrollMove":
                robot.mouseWheel((int)(packet.getInt("deltaY")/30));
                break;
            case "ScrollClick":
                robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
            default:
                LOGGER.log(Level.SEVERE, "MouseController.handleIncomingPacket: invalid keyboard action");
        }
    }
}

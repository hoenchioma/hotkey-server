package com.rfw.hotkey_server.control;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PowerPointController {
    private static final Logger LOGGER = Logger.getLogger(PowerPointController.class.getName());
    private Robot robot;
    private Window w;

    int xPos, yPos;

    public PowerPointController() {
        // TODO: Fix pointer issue
//        xPos = 0;
//        yPos = 0;
//        w  =new Window(null)
//        {
//            @Override
//            public void paint(Graphics g)
//            {
//                g.setColor(Color.RED);
//                //xPos = getWidth() / 2;
//                //yPos = getHeight() / 2;
//                g.fillOval(xPos,yPos,10,10);
//            }
//            @Override
//            public void update(Graphics g)
//            {
//                //g.setColor(Color.RED);
//               // g.fillOval(xPos, yPos, 10, 10);
//                paint(g);
//            }
//        };
//        w.setAlwaysOnTop(true);
//        w.setBounds(w.getGraphicsConfiguration().getBounds());
//        w.setBackground(new Color(0, true));
//        w.setVisible(true);
//
//
//
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
            case "beginning" :
                type(KeyEvent.VK_F5);
                break;
            case "current" :
                keyPress(KeyEvent.VK_SHIFT);
                keyPress(KeyEvent.VK_F5);
                robot.delay(10);
                keyRelease(KeyEvent.VK_F5);
                keyRelease(KeyEvent.VK_SHIFT);
                break;
            case "ESC" :
                type(KeyEvent.VK_ESCAPE);
                break;
            case "UP":
                type(KeyEvent.VK_UP);
//                xPos+=100;
//                w.repaint(xPos,yPos,10,10);
                //w.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
    public void handleIncomingPacket(JSONObject packet) {
        String action = packet.getString("action");
        switch (action) {

            case "modifier" :
                pressModifierButton(packet.getString("key"));
            case "TouchpadMove":
                //xPos += packet.getInt("deltaX");
                //yPos += packet.getInt("deltaY");
//                LOGGER.log(Level.SEVERE,xPos+" "+yPos);
//                System.out.println(xPos+" "+yPos);
                break;
                // TODO: (Wadith) implement other actions
            default:
                LOGGER.log(Level.SEVERE, "PowerPointController.handleIncomingPacket: invalid powerpoint action\n");
        }
    }
}

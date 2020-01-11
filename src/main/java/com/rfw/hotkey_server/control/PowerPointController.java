package com.rfw.hotkey_server.control;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to control the presentation / slideshow application (Powerpoint)
 *
 * @author Shadman Wadith
 */
public class PowerPointController {
    private static final Logger LOGGER = Logger.getLogger(PowerPointController.class.getName());

    private static final int PADDING = 5; // pointer window padding
    private static final int POINTER_SIZE = 30; // size of pointer
    private static final int POINTER_SPEED = 10; // speed of pointer

    private Robot robot;

    private JFrame pointerWindow = null;

    public PowerPointController() throws AWTException {
        robot = new Robot();

        //showPointer();
        //movePointer(100,100);
    }
    private void keyPress(int keyCode) {
        robot.keyPress(keyCode);
    }

    private void keyRelease(int keyCode) {
        robot.keyRelease(keyCode);
    }

    private void type(int keyCode) {
        keyPress(keyCode);
        robot.delay(50);
        keyRelease(keyCode);
    }

    private void pressModifierButton(String keyword) {
        switch (keyword) {
            case "beginning":
                type(KeyEvent.VK_F5);
                break;
            case "current":
                keyPress(KeyEvent.VK_SHIFT);
                keyPress(KeyEvent.VK_F5);
                robot.delay(10);
                keyRelease(KeyEvent.VK_F5);
                keyRelease(KeyEvent.VK_SHIFT);
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

    public void handleIncomingPacket(JSONObject packet) {
        String action = packet.getString("action");
        switch (action) {

            case "modifier":
                pressModifierButton(packet.getString("key"));
                break;
            case "pointer" :
                int deltaX = packet.getInt("deltaX");
                int deltaY = packet.getInt("deltaY");
                movePointer(deltaX, deltaY);
//                PPTPointer.movePointer(deltaX,deltaY);
//                LOGGER.log(Level.INFO, deltaX +","+ deltaY);
                break;
            case "point":
                String pointerStatus = packet.getString("key");
                if(pointerStatus.equals("on")) showPointer();
                else if(pointerStatus.equals("off")) hidePointer();
            default:
                LOGGER.log(Level.SEVERE, "PowerPointController.handleIncomingPacket: invalid powerpoint action\n");
        }
    }

    private void showPointer() {
        hidePointer();
        pointerWindow = makePointerWindow();
    }

    private void hidePointer() {
        if (pointerWindow != null) {
            pointerWindow.dispose();
            pointerWindow = null;
        }
    }

    private void movePointer(int deltaX, int deltaY) {
        SwingUtilities.invokeLater(() -> {
            if (pointerWindow != null) {
                JFrame window = pointerWindow;
                window.setLocation(
                        window.getLocation().x + deltaX,
                        window.getLocation().y + deltaY
                );
            }
        });
    }

    private static JFrame makePointerWindow() {
        JFrame window = new JFrame() {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.RED);
                g.fillOval(PADDING, PADDING, POINTER_SIZE / 2, POINTER_SIZE / 2);
            }
        };
        try {
            SwingUtilities.invokeAndWait(() -> {
                window.setUndecorated(true); // remove border and title bar
                window.setAlwaysOnTop(true); // make window always appear on top
                window.setBackground(new Color(0, 0, 0, 0)); // make background transparent
                window.setSize(POINTER_SIZE + PADDING * 2, POINTER_SIZE + PADDING * 2);
    //            window.pack();
                window.setLocationRelativeTo(null); // put window at the center of screen
                window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // set window to not close application when disposed
                window.setVisible(true);
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return window;
    }

    public static void main(String[] args) throws AWTException {
        PowerPointController powerPointController = new PowerPointController();
        powerPointController.showPointer();
        JFrame window = powerPointController.pointerWindow;

        // code to move window using arrow keys (for testing purposes)
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:  powerPointController.movePointer(-POINTER_SPEED, 0); break;
                    case KeyEvent.VK_RIGHT: powerPointController.movePointer(POINTER_SPEED, 0); break;
                    case KeyEvent.VK_UP:    powerPointController.movePointer(0, -POINTER_SPEED); break;
                    case KeyEvent.VK_DOWN:  powerPointController.movePointer(0, POINTER_SPEED); break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }

    public void cleanUp() {
        hidePointer();
    }
}

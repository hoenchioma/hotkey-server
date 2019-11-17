package com.rfw.hotkey_server.control;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PowerPointController {
    private static final Logger LOGGER = Logger.getLogger(PowerPointController.class.getName());

    private static final int PADDING = 5; // pointer window padding
    private static final int POINTER_SIZE = 30; // size of pointer
    private static final int POINTER_SPEED = 10; // speed of pointer

    private Robot robot;

    private JFrame pointerWindow = null;
    private final Object pointerMonitor = new Object();

    public PowerPointController() {
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
                break;
            case "pointer" :
//                pointerX += Integer.parseInt(packet.getString("deltaX"));
//                pointerY += Integer.parseInt(packet.getString("deltaY"));
//                PPTPointer.movePointer(pointerX,pointerY);
//                LOGGER.log(Level.SEVERE,pointerX+","+pointerY);
//                // TODO: (Wadith) implement other actions
            default:
                LOGGER.log(Level.SEVERE, "PowerPointController.handleIncomingPacket: invalid powerpoint action\n");
        }
    }

    private void showPointer() {
        synchronized (pointerMonitor) {
            pointerWindow = makePointerWindow();
        }
    }

    private void hidePointer() {
        synchronized (pointerMonitor) {
            if (pointerWindow != null) {
                pointerWindow.dispose();
                pointerWindow = null;
            }
        }
    }

    private void movePointer(int deltaX, int deltaY) {
        SwingUtilities.invokeLater(() -> {
            synchronized (pointerMonitor) {
                if (pointerWindow != null) {
                    JFrame window = pointerWindow;
                    window.setLocation(
                            window.getLocation().x + deltaX,
                            window.getLocation().y + deltaY
                    );
                }
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

    public static void main(String[] args) {
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
}

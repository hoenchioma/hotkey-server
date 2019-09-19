package com.rfw.hotkey_server.util;

import javax.swing.*;
import java.awt.*;
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
}

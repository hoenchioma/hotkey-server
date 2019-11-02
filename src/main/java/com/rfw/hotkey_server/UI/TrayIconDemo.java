package com.rfw.hotkey_server.UI;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;

public class TrayIconDemo {

    int serverStatus = 0;
    // serverStatus=0=off
    // serverStatus=1=on
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void createAndShowGUI() throws IOException {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();

        Image img =Toolkit.getDefaultToolkit().getImage("C:/Users/ASUS/Projects/hotkey/hotkey-server/src/main/resources/bulb.gif");
        final SystemTray tray = SystemTray.getSystemTray();
        final TrayIcon trayIcon = new TrayIcon(img, "HotKeys", popup);


        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("About");
        MenuItem settingItem = new MenuItem("Setting");
        MenuItem startServerItem = new MenuItem("Start Server");

        MenuItem exitItem = new MenuItem("Exit");

        //Add components to popup menu

        popup.add(startServerItem);
        popup.add(settingItem);
        popup.addSeparator();
        popup.add(aboutItem);

        popup.add(exitItem);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

        startServerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
                startServerItem.setLabel("Stop");
            }
        });
        settingItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO setting will open
            }
        });
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from the About menu item");
            }
            //TODO about Selected
        });



        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
    }


}


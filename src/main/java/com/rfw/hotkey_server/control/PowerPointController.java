package com.rfw.hotkey_server.control;

import com.rfw.hotkey_server.ui.PPTPointer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PowerPointController {
    private static final Logger LOGGER = Logger.getLogger(PowerPointController.class.getName());
    private Robot robot;
   // private PPTPointer pptPointer;
    private int pointerX,pointerY;
    public PowerPointController() {
        try {
            robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error Occurred!");
        }

        //primaryStage.setTitle("JavaFX App");
        Circle circle = new Circle(150.0f, 150.0f, 100.f);
        circle.setFill(Color.RED);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Group group = new Group(circle);

        // create a scene
        Scene scene = new Scene(group, 500, 300);
        //stage.initModality(Modality.WINDOW_MODAL);
        //stage.initModality(Modality.NONE);
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.show();
        stage.showAndWait();

        //stage.showAndWait();
        pointerX = 400;
        pointerY = 400;

        //pptPointer = new PPTPointer();
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
                PPTPointer.movePointer(pointerX+10,pointerY);
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
                pointerX += Integer.parseInt(packet.getString("deltaX"));
                pointerY += Integer.parseInt(packet.getString("deltaY"));
                PPTPointer.movePointer(pointerX,pointerY);
                LOGGER.log(Level.SEVERE,pointerX+","+pointerY);
                // TODO: (Wadith) implement other actions
            default:
                LOGGER.log(Level.SEVERE, "PowerPointController.handleIncomingPacket: invalid powerpoint action\n");
        }
    }
}

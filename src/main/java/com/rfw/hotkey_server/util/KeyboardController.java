package com.rfw.hotkey_server.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyboardController {
    Robot robot;
    public KeyboardController(){
        try{
            robot = new Robot();
        }
        catch(Exception e){
            e.printStackTrace();;
            JOptionPane.showMessageDialog(null,"Error Occurred!");
        }
    }
    public void keyPress(int keyCode) {
        robot.keyPress(keyCode);
    }
    public void keyRelease(int keyCode) {
        robot.keyRelease(keyCode);
    }
    public void ctrlS(){
        keyPress(KeyEvent.VK_CONTROL);
        keyPress(KeyEvent.VK_S);
        robot.delay(10);
        keyRelease(KeyEvent.VK_S);
        keyRelease(KeyEvent.VK_CONTROL);
    }
    public void ctrlV(){
        keyPress(KeyEvent.VK_CONTROL);
        keyPress(KeyEvent.VK_V);
        robot.delay(10);
        keyRelease(KeyEvent.VK_V);
        keyRelease(KeyEvent.VK_CONTROL);
    }
    public void ctrlAltT() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_T);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_T);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }
    public void ctrlAltL() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_L);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_L);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }
    public void ctrlShiftZ() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_Z);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_Z);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }
    public void altF4() {
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_F4);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_F4);
        robot.keyRelease(KeyEvent.VK_ALT);
    }
    public void type(int asciiCode){

        keyPress(asciiCode);
        robot.delay(10);
        keyRelease(asciiCode);

    }
    public void pressLeftArrowKey() {
        type(KeyEvent.VK_LEFT);
    }
    public void pressDownArrowKey() {
        type(KeyEvent.VK_DOWN);
    }
    public void pressRightArrowKey() {
        type(KeyEvent.VK_RIGHT);
    }
    public void pressUpArrowKey() {
        type(KeyEvent.VK_UP);
    }

    public void pressF5Key() {
        type(KeyEvent.VK_F5);
    }



}

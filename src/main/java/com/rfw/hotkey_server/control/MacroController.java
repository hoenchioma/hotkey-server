package com.rfw.hotkey_server.control;

import org.json.JSONObject;

import java.util.*;
import java.util.logging.Logger;

/**
 * Controller for handling and executing macro key combinations.
 *
 * @author Farhan Kabir
 */

public class MacroController {
    private static final Logger LOGGER = Logger.getLogger(MacroController.class.getName());

    private BaseKeyboardController keyboard;

    public MacroController(BaseKeyboardController keyboard) {
        this.keyboard = keyboard;
    }

    public void handle(JSONObject packet) {
        int totalKeys = packet.getInt("size");
        Deque<Integer> keys = new ArrayDeque<>();

        for (int i = 0; i < totalKeys; i++) {
            String keyword = packet.getString(String.valueOf(i));
            int modifierKeyCode = keyboard.getModifierKeyCode(keyword);
            if (modifierKeyCode != -1) { // the key is a modifier
                keys.addFirst(modifierKeyCode); // add to front
            } else {
                keys.addLast(keyboard.getKeyCode(keyword)); // add to last
            }
        }

        keyboard.typeKeys(keys);
    }
}

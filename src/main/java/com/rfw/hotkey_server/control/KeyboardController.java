package com.rfw.hotkey_server.control;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to handle Keyboard actions from client
 *
 * @author Shadman Wadith
 * @author Raheeb Hassan
 */
public class KeyboardController {
    private static final Logger LOGGER = Logger.getLogger(KeyboardController.class.getName());

    private BaseKeyboardController keyboard;

    public KeyboardController(BaseKeyboardController keyboard) {
        this.keyboard = keyboard;
    }

    /**
     * Handles incoming packet from client
     * @param packet JSON packet
     */
    public void handle(JSONObject packet) {
//        LOGGER.log(Level.INFO, "KeyboardController.handle: " + packet);
        String actionType = packet.getString("action");

        int[] keys = new int[0];
        if (packet.has("key")) {
            String key = packet.getString("key"); // extract key
            keys = keyboard.getKeyCodes(key); // decode key
            if (keys == null) {
                LOGGER.log(Level.SEVERE, "KeyboardController.handle: keyCode for " + key + " not found");
                LOGGER.log(Level.INFO, "KeyboardController.handle: aborting key press operation");
                return;
            }
        }

        // extract modifiers (to a string ArrayList)
        int[] modifiers = new int[0];
        if (packet.has("modifiers")) {
            JSONArray modifiersJson = packet.getJSONArray("modifiers"); // get json array
            modifiers = new int[modifiersJson.length()];
            for (int i = 0; i < modifiers.length; i++) {
                modifiers[i] = keyboard.getModifierKeyCode(modifiersJson.getString(i)); // put decoded key code in array
                if (modifiers[i] == -1) {
                    LOGGER.log(Level.SEVERE, "KeyboardController.handle: modifier keyCode for " + modifiersJson.getString(i) + " not found");
                    LOGGER.log(Level.INFO, "KeyboardController.handle: aborting key press operation");
                    return;
                }
            }
        }

        try {
            switch (actionType) {
                case "press":
                    keyboard.pressKeys(modifiers);
                    keyboard.pressKeys(keys);
                    break;
                case "release":
                    keyboard.releaseKeys(keys);
                    keyboard.releaseKeys(modifiers);
                    break;
                case "type":
                    keyboard.pressKeys(modifiers);
                    keyboard.typeKeys(keys);
                    keyboard.releaseKeys(modifiers);
                    break;
                case "clear":
                    keyboard.releaseKeys();
                    break;
                default:
                    throw new IllegalStateException("Unknown action type " + actionType);
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            LOGGER.log(Level.SEVERE, "KeyboardController.handle: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

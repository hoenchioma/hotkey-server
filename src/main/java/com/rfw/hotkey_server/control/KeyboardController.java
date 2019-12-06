package com.rfw.hotkey_server.control;

import com.rfw.hotkey_server.util.misc.StackSet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.awt.event.KeyEvent.*;

/**
 * A class to handle Keyboard actions from client
 *
 * @author Shadman Wadith
 * @author Raheeb Hassan
 */
public class KeyboardController {
    private static final Logger LOGGER = Logger.getLogger(KeyboardController.class.getName());

    private static final int DEFAULT_KEY_DELAY = 50;

    private final Robot robot;
    private final StackSet<Integer> keyStack = new StackSet<>();

    public KeyboardController() throws AWTException {
        robot = new Robot();
    }

    void pressKey(int keyCode) {
        robot.keyPress(keyCode);
        keyStack.pushIfAbsent(keyCode);
//        LOGGER.log(Level.INFO, "KeyboardController.pressKey: pressed " + keyCode);
    }

    void pressKeys(int... keyCodes) {
        for (int i: keyCodes) pressKey(i);
    }

    void releaseSomeKeys(int noOfKeys) {
        while (!keyStack.isEmpty() && noOfKeys-- > 0) {
//            LOGGER.log(Level.INFO, "KeyboardController.releaseSomeKeys: released key " + keyStack.peek());
            robot.keyRelease(keyStack.popIfPresent());
        }
    }

    // release all currently pressed keys
    void releaseKeys() {
        releaseSomeKeys(keyStack.size());
    }

    void releaseKey(int keyCode) {
        if (keyStack.contains(keyCode)) {
            robot.keyRelease(keyCode);
            keyStack.remove(keyCode);
//            LOGGER.log(Level.INFO, "KeyboardController.releaseSomeKeys: released key " + keyCode);
        }
    }

    void releaseKeys(int... keyCodes) {
        for (int i = keyCodes.length-1; i >= 0; i--) {
            releaseKey(keyCodes[i]);
        }
    }

    private void typeKeys(int... keyCodes) {
        typeKeysWithDelay(DEFAULT_KEY_DELAY, keyCodes);
    }

    private void typeKeysWithDelay(int delay, int... keyCodes) {
        pressKeys(keyCodes);
        robot.delay(delay);
        releaseSomeKeys(keyCodes.length);
    }

    private void typeKey(int keyCode) {
        typeKeyWithDelay(DEFAULT_KEY_DELAY, keyCode);
    }

    private void typeKeyWithDelay(int delay, int keyCode) {
        typeKeysWithDelay(keyCode);
    }

    public int[] getKeyCode(char c) throws IllegalArgumentException {
        switch (c) {
            // alphabets
            case 'a':	return new int[] {VK_A};
            case 'b':	return new int[] {VK_B};
            case 'c':	return new int[] {VK_C};
            case 'd':	return new int[] {VK_D};
            case 'e':	return new int[] {VK_E};
            case 'f':	return new int[] {VK_F};
            case 'g':	return new int[] {VK_G};
            case 'h':	return new int[] {VK_H};
            case 'i':	return new int[] {VK_I};
            case 'j':	return new int[] {VK_J};
            case 'k':	return new int[] {VK_K};
            case 'l':	return new int[] {VK_L};
            case 'm':	return new int[] {VK_M};
            case 'n':	return new int[] {VK_N};
            case 'o':	return new int[] {VK_O};
            case 'p':	return new int[] {VK_P};
            case 'q':	return new int[] {VK_Q};
            case 'r':	return new int[] {VK_R};
            case 's':	return new int[] {VK_S};
            case 't':	return new int[] {VK_T};
            case 'u':	return new int[] {VK_U};
            case 'v':	return new int[] {VK_V};
            case 'w':	return new int[] {VK_W};
            case 'x':	return new int[] {VK_X};
            case 'y':	return new int[] {VK_Y};
            case 'z':	return new int[] {VK_Z};
            case 'A':	return new int[] {VK_SHIFT, VK_A};
            case 'B':	return new int[] {VK_SHIFT, VK_B};
            case 'C':	return new int[] {VK_SHIFT, VK_C};
            case 'D':	return new int[] {VK_SHIFT, VK_D};
            case 'E':	return new int[] {VK_SHIFT, VK_E};
            case 'F':	return new int[] {VK_SHIFT, VK_F};
            case 'G':	return new int[] {VK_SHIFT, VK_G};
            case 'H':	return new int[] {VK_SHIFT, VK_H};
            case 'I':	return new int[] {VK_SHIFT, VK_I};
            case 'J':	return new int[] {VK_SHIFT, VK_J};
            case 'K':	return new int[] {VK_SHIFT, VK_K};
            case 'L':	return new int[] {VK_SHIFT, VK_L};
            case 'M':	return new int[] {VK_SHIFT, VK_M};
            case 'N':	return new int[] {VK_SHIFT, VK_N};
            case 'O':	return new int[] {VK_SHIFT, VK_O};
            case 'P':	return new int[] {VK_SHIFT, VK_P};
            case 'Q':	return new int[] {VK_SHIFT, VK_Q};
            case 'R':	return new int[] {VK_SHIFT, VK_R};
            case 'S':	return new int[] {VK_SHIFT, VK_S};
            case 'T':	return new int[] {VK_SHIFT, VK_T};
            case 'U':	return new int[] {VK_SHIFT, VK_U};
            case 'V':	return new int[] {VK_SHIFT, VK_V};
            case 'W':	return new int[] {VK_SHIFT, VK_W};
            case 'X':	return new int[] {VK_SHIFT, VK_X};
            case 'Y':	return new int[] {VK_SHIFT, VK_Y};
            case 'Z':	return new int[] {VK_SHIFT, VK_Z};
            // numbers
            case '0':	return new int[] {VK_0};
            case '1':	return new int[] {VK_1};
            case '2':	return new int[] {VK_2};
            case '3':	return new int[] {VK_3};
            case '4':	return new int[] {VK_4};
            case '5':	return new int[] {VK_5};
            case '6':	return new int[] {VK_6};
            case '7':	return new int[] {VK_7};
            case '8':	return new int[] {VK_8};
            case '9':	return new int[] {VK_9};
            // special characters
            case '`':	return new int[] {VK_BACK_QUOTE};
            case '-':	return new int[] {VK_MINUS};
            case '=':	return new int[] {VK_EQUALS};
            case '~':	return new int[] {VK_BACK_QUOTE};
            case '!':	return new int[] {VK_SHIFT, VK_1};
            case '@':	return new int[] {VK_SHIFT, VK_2};
            case '#':	return new int[] {VK_SHIFT, VK_3};
            case '$':	return new int[] {VK_SHIFT, VK_4};
            case '%':	return new int[] {VK_SHIFT, VK_5};
            case '^':	return new int[] {VK_SHIFT, VK_6};
            case '&':	return new int[] {VK_SHIFT, VK_7};
            case '*':	return new int[] {VK_SHIFT, VK_8};
            case '(':	return new int[] {VK_SHIFT, VK_9};
            case ')':	return new int[] {VK_SHIFT, VK_0};
            case '_':	return new int[] {VK_SHIFT, VK_MINUS};
            case '+':	return new int[] {VK_SHIFT, VK_EQUALS};
            case '\t':	return new int[] {VK_TAB};
            case '\n':	return new int[] {VK_ENTER};
            case '[':	return new int[] {VK_OPEN_BRACKET};
            case ']':	return new int[] {VK_CLOSE_BRACKET};
            case '\\':	return new int[] {VK_BACK_SLASH};
            case '{':	return new int[] {VK_SHIFT, VK_OPEN_BRACKET};
            case '}':	return new int[] {VK_SHIFT, VK_CLOSE_BRACKET};
            case '|':	return new int[] {VK_SHIFT, VK_BACK_SLASH};
            case ';':	return new int[] {VK_SEMICOLON};
            case ':':	return new int[] {VK_SHIFT, VK_SEMICOLON};
            case '\'':	return new int[] {VK_QUOTE};
            case '"':	return new int[] {VK_SHIFT, VK_QUOTE};
            case ',':	return new int[] {VK_COMMA};
            case '<':	return new int[] {VK_SHIFT, VK_COMMA};
            case '.':	return new int[] {VK_PERIOD};
            case '>':	return new int[] {VK_SHIFT, VK_PERIOD};
            case '/':	return new int[] {VK_SLASH};
            case '?':	return new int[] {VK_SHIFT, VK_SLASH};
            case ' ':	return new int[] {VK_SPACE};
            case '\b':	return new int[] {VK_BACK_SPACE};
            default: throw new IllegalArgumentException("Unknown character " + c);
        }
    }

    public int[] getKeyCode(String keyword) throws IllegalArgumentException {
        switch (keyword) {
            case "ESC":	    return new int[] {VK_ESCAPE};
            case "HOME":	return new int[] {VK_HOME};
            case "END":     return new int[] {VK_END};
            case "PGUP":	return new int[] {VK_PAGE_UP};
            case "PGDN":	return new int[] {VK_PAGE_DOWN};
            case "UP":	    return new int[] {VK_UP};
            case "DOWN":	return new int[] {VK_DOWN};
            case "LEFT":	return new int[] {VK_LEFT};
            case "RIGHT":	return new int[] {VK_RIGHT};
            case "TAB":	    return new int[] {VK_TAB};
            case "BSPACE":  return new int[] {VK_BACK_SPACE};
            case "PRTSC":   return new int[] {VK_PRINTSCREEN};
            case "COPY":    return new int[] {VK_CONTROL, VK_C};
            case "PASTE":   return new int[] {VK_CONTROL, VK_V};
            default:
                // try matching single character code
                if (keyword.length() == 1) return getKeyCode(keyword.charAt(0));
                // try matching modifier keycode
                else return new int[] {getModifierKeyCode(keyword)};
        }
    }

    public int getModifierKeyCode(String keyword) throws IllegalArgumentException {
        switch (keyword) {
            case "SHIFT": return VK_SHIFT;
            case "CTRL":  return VK_CONTROL;
            case "ALT":   return VK_ALT;
            case "WIN":   return VK_WINDOWS;
            default: throw new IllegalArgumentException("Unknown keyword " + keyword);
        }
    }

    private void pressModifiers(ArrayList<String> modifiers) {
        for (int i = 0, n = modifiers.size(); i < n; i++) {
            pressKey(getModifierKeyCode(modifiers.get(i)));
        }
    }

    private void releaseModifiers(ArrayList<String> modifiers) {
        for (int i = modifiers.size()-1; i >= 0; i--) {
            releaseKey(getModifierKeyCode(modifiers.get(i)));
        }
    }

    // buffer to hold String ArrayList of modifiers
    private final ArrayList<String> _modifiers = new ArrayList<>();

    /**
     * Handles incoming packet from client
     * @param packet JSON packet
     */
    public void handleIncomingPacket(JSONObject packet) {
        String actionType = packet.getString("action");

        // extract key
        String key = null;
        if (packet.has("key")) {
            key = packet.getString("key");
        }

        // extract modifiers (to a string ArrayList)
        _modifiers.clear();
        if (packet.has("modifiers")) {
            JSONArray modifiersJson = packet.getJSONArray("modifiers");
            for (int i = 0, n = modifiersJson.length(); i < n; i++) {
                _modifiers.add(modifiersJson.getString(i));
            }
        }

        try {
            switch (actionType) {
                case "press":
                    pressModifiers(_modifiers);
                    if (key != null) pressKeys(getKeyCode(key));
                    break;
                case "release":
                    if (key != null) releaseKeys(getKeyCode(key));
                    else releaseKeys();
                    releaseModifiers(_modifiers);
                    break;
                case "type":
                    pressModifiers(_modifiers);
                    if (key != null) typeKeys(getKeyCode(key));
                    releaseModifiers(_modifiers);
                    break;
                default:
                    throw new IllegalStateException("Unknown action type " + actionType);
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            LOGGER.log(Level.SEVERE, "KeyboardController.handleIncomingPacket: " + e.getMessage());
        }
    }

    public void stop() {
        releaseKeys();
    }
}

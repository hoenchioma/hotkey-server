package com.rfw.hotkey_server.control;

import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static java.awt.event.KeyEvent.*;
import static java.awt.event.KeyEvent.VK_SHIFT;

public class BaseKeyboardController {
    private static final Logger LOGGER = Logger.getLogger(KeyboardController.class.getName());

    private static final int DEFAULT_KEY_DELAY = 50;

    private final Robot robot = new Robot();

    private final Set<Integer> pressedKeys = new HashSet<>(); // hashset to keep track of pressed keys

    public BaseKeyboardController() throws AWTException {}

    void pressKey(int keyCode) {
        robot.keyPress(keyCode);
        pressedKeys.add(keyCode);
//        LOGGER.log(Level.INFO, "KeyboardController.pressKey: pressed " + keyCode);
    }

    void pressKeys(int... keyCodes) {
        for (int i: keyCodes) pressKey(i);
    }

    void pressKeys(java.util.List<Integer> keyCodes) {
        for (int i: keyCodes) pressKey(i);
    }

    void releaseKey(int keyCode) {
        robot.keyRelease(keyCode);
        pressedKeys.remove(keyCode);
//        LOGGER.log(Level.INFO, "KeyboardController.releaseSomeKeys: released key " + keyCode);
    }

    void releaseKeys(int... keyCodes) {
        // releases keys in REVERSE order
        for (int i = keyCodes.length-1; i >= 0; i--) {
            releaseKey(keyCodes[i]);
        }
    }

    void releaseKeys(List<Integer> keyCodes) {
        for (int i: Lists.reverse(keyCodes)) pressKey(i);
    }

    // release all currently pressed keys
    void releaseKeys() {
        releaseKeys(new ArrayList<>(pressedKeys));
    }

    void typeKey(int keyCode) {
        pressKey(keyCode);
        robot.delay(DEFAULT_KEY_DELAY);
        releaseKey(keyCode);
    }

    void typeKeys(int... keyCodes) {
        pressKeys(keyCodes);
        robot.delay(DEFAULT_KEY_DELAY);
        releaseKeys(keyCodes);
    }

    /**
     * Get the keycode corresponding to char c
     * @param c character
     * @return keycode of c, if not found -1
     */
    public int getCharKeyCode(char c) {
        switch (c) {
            // alphabets
            case 'a':	return VK_A;
            case 'b':	return VK_B;
            case 'c':	return VK_C;
            case 'd':	return VK_D;
            case 'e':	return VK_E;
            case 'f':	return VK_F;
            case 'g':	return VK_G;
            case 'h':	return VK_H;
            case 'i':	return VK_I;
            case 'j':	return VK_J;
            case 'k':	return VK_K;
            case 'l':	return VK_L;
            case 'm':	return VK_M;
            case 'n':	return VK_N;
            case 'o':	return VK_O;
            case 'p':	return VK_P;
            case 'q':	return VK_Q;
            case 'r':	return VK_R;
            case 's':	return VK_S;
            case 't':	return VK_T;
            case 'u':	return VK_U;
            case 'v':	return VK_V;
            case 'w':	return VK_W;
            case 'x':	return VK_X;
            case 'y':	return VK_Y;
            case 'z':	return VK_Z;
            // numbers
            case '0':	return VK_0;
            case '1':	return VK_1;
            case '2':	return VK_2;
            case '3':	return VK_3;
            case '4':	return VK_4;
            case '5':	return VK_5;
            case '6':	return VK_6;
            case '7':	return VK_7;
            case '8':	return VK_8;
            case '9':	return VK_9;
            // symbols
            case '`':	return VK_BACK_QUOTE;
            case '=':	return VK_EQUALS;
            case '-':	return VK_MINUS;
            case '\t':	return VK_TAB;
            case '\n':	return VK_ENTER;
            case '[':	return VK_OPEN_BRACKET;
            case ']':	return VK_CLOSE_BRACKET;
            case '\\':	return VK_BACK_SLASH;
            case ';':	return VK_SEMICOLON;
            case '\'':	return VK_QUOTE;
            case ',':	return VK_COMMA;
            case '.':	return VK_PERIOD;
            case '/':	return VK_SLASH;
            case ' ':	return VK_SPACE;
            case '\b':	return VK_BACK_SPACE;
            // if not found
            default: return -1;
        }
    }

    /**
     * Get keycode corresponding to char c
     * (you have to press Shift + keycode to print c)
     * @param c character
     * @return keycode of c, -1 otherwise
     */
    public int getSecondaryCharKeyCode(char c)  {
        switch (c) {
            // alphabets
            case 'A':	return VK_A;
            case 'B':	return VK_B;
            case 'C':	return VK_C;
            case 'D':	return VK_D;
            case 'E':	return VK_E;
            case 'F':	return VK_F;
            case 'G':	return VK_G;
            case 'H':	return VK_H;
            case 'I':	return VK_I;
            case 'J':	return VK_J;
            case 'K':	return VK_K;
            case 'L':	return VK_L;
            case 'M':	return VK_M;
            case 'N':	return VK_N;
            case 'O':	return VK_O;
            case 'P':	return VK_P;
            case 'Q':	return VK_Q;
            case 'R':	return VK_R;
            case 'S':	return VK_S;
            case 'T':	return VK_T;
            case 'U':	return VK_U;
            case 'V':	return VK_V;
            case 'W':	return VK_W;
            case 'X':	return VK_X;
            case 'Y':	return VK_Y;
            case 'Z':	return VK_Z;
            // special characters
            case '!':	return VK_1;
            case '@':	return VK_2;
            case '#':	return VK_3;
            case '$':	return VK_4;
            case '%':	return VK_5;
            case '^':	return VK_6;
            case '&':	return VK_7;
            case '*':	return VK_8;
            case '(':	return VK_9;
            case ')':	return VK_0;
            case '_':	return VK_MINUS;
            case '+':	return VK_EQUALS;
            case '{':	return VK_OPEN_BRACKET;
            case '}':	return VK_CLOSE_BRACKET;
            case '|':	return VK_BACK_SLASH;
            case ':':	return VK_SEMICOLON;
            case '"':	return VK_QUOTE;
            case '<':	return VK_COMMA;
            case '>':	return VK_PERIOD;
            case '?':	return VK_SLASH;
            // if not found return null
            default:    return -1;
        }
    }

    /**
     * Get the keycode corresponding to keyword
     * @param keyword keyword representing the key (in any case)
     * @return the keycode, -1 if not found
     */
    public int getSpecialKeyCode(String keyword) {
        switch (keyword.toUpperCase()) {
            case "ESCAPE":
            case "ESC":	        return VK_ESCAPE;
            case "HOME":	    return VK_HOME;
            case "END":         return VK_END;
            case "UP":	        return VK_UP;
            case "DOWN":	    return VK_DOWN;
            case "LEFT":	    return VK_LEFT;
            case "RIGHT":	    return VK_RIGHT;
            case "TAB":	        return VK_TAB;
            case "SPACE":       return VK_SPACE;
            case "ENTER":       return VK_ENTER;
            case "INSERT":
            case "INS":         return VK_INSERT;
            case "DELETE":
            case "DEL":         return VK_DELETE;
            case "BACKSPACE":
            case "BACK_SPACE":
            case "BSPACE":      return VK_BACK_SPACE;
            case "PRINTSCREEN":
            case "PRINT_SCREEN":
            case "PRTSC":       return VK_PRINTSCREEN;
            case "PAGEUP":
            case "PAGE_UP":
            case "PGUP":        return VK_PAGE_UP;
            case "PAGEDOWN":
            case "PAGE_DOWN":
            case "PGDN":        return VK_PAGE_DOWN;
            default: return -1;
        }
    }

    /**
     * Get modifier key code
     * (key pressed with other keys)
     * @param keyword to identify key (in any case)
     * @return keycode, -1 if not found
     */
    public int getModifierKeyCode(String keyword) {
        switch (keyword.toUpperCase()) {
            case "SHIFT": return VK_SHIFT;
            case "CTRL":  return VK_CONTROL;
            case "ALT":   return VK_ALT;
            case "WIN":   return VK_WINDOWS;
            default:      return -1;
        }
    }

    /**
     * Get combination of key codes for a command
     * @param keyword String representing commands
     * @return keycode, null if not found
     */
    public @Nullable
    int[] getCommandKeyCodes(String keyword) {
        switch (keyword.toUpperCase()) {
            case "COPY":    return new int[]{VK_CONTROL, VK_C};
            case "PASTE":   return new int[]{VK_CONTROL, VK_V};
            default: return null;
        }
    }

    public int getKeyCode(String keyword) {
        int res;
        if (keyword.length() == 1) {
            if ((res = getCharKeyCode(keyword.charAt(0))) != -1) return res;
            else return -1;
        } else {
            if ((res = getSpecialKeyCode(keyword)) != -1) return res;
            else if ((res = getModifierKeyCode(keyword)) != -1) return res;
            else return -1;
        }
    }

    public int[] getKeyCodes(String keyword) {
        int res;
        int[] resArr;
        if (keyword.length() == 1) {
            if ((res = getCharKeyCode(keyword.charAt(0))) != -1) return new int[] {res};
            else if ((res = getSecondaryCharKeyCode(keyword.charAt(0))) != -1) return new int[] {VK_SHIFT, res};
            else return null;
        } else {
            if ((res = getSpecialKeyCode(keyword)) != -1) return new int[] {res};
            else if ((res = getModifierKeyCode(keyword)) != -1) return new int[] {res};
            else if ((resArr = getCommandKeyCodes(keyword)) != null) return resArr;
            else return null;
        }
    }

    public void cleanUp() {
        releaseKeys();
    }
}

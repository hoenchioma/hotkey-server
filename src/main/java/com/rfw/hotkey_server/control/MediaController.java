package com.rfw.hotkey_server.control;

import com.rfw.hotkey_server.commands.MediaKeys;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MediaController {
    private static final Logger LOGGER = Logger.getLogger(MediaController.class.getName());

    public void handleIncomingPacket(JSONObject packet) {
        String action = packet.getString("action");
        switch (action) {
            case "volumeUp":
                MediaKeys.volumeUp();
                break;
            case "volumeDown":
                MediaKeys.volumeDown();
                break;
            case "playPause":
                MediaKeys.songPlayPause();
                break;
            case "next":
                MediaKeys.songNext();
                break;
            case "prev":
                MediaKeys.songPrevious();
                break;
            case "mute":
                MediaKeys.volumeMute();
                break;
            default:
                LOGGER.log(Level.SEVERE, "MediaController.handleIncomingPacket: unknown package action (" + action + ")");
        }
    }
}

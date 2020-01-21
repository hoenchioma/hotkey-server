package com.rfw.hotkey_server.ui;

import com.jfoenix.controls.JFXButton;
import com.rfw.hotkey_server.net.BluetoothServer;
import com.rfw.hotkey_server.net.ConnectionType;
import com.rfw.hotkey_server.net.Server;
import com.rfw.hotkey_server.net.WiFiServer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import javax.annotation.Nullable;
import javax.bluetooth.BluetoothStateException;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.rfw.hotkey_server.util.Device.getLocalIpAddress;
import static com.rfw.hotkey_server.util.Utils.showQRCode;

/**
 * FXML controller for the home screen scene
 *
 * @author Shadman Wadith
 * @author Raheeb Hassan
 */
public class HomeScreenViewController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(HomeScreenViewController.class.getName());

    private static final String STATUS_LABEL_ONLINE = "Online";
    private static final String STATUS_LABEL_OFFLINE = "Offline";

    private static final String START_BUTTON_TEXT = "START";
    private static final String STOP_BUTTON_TEXT = "STOP";

    private static final String NOT_CONNECTED_TEXT = "Not Connected";

    private static final Paint CONNECTED_COLOR = Paint.valueOf("lime");
    private static final Paint BASE_COLOR = Paint.valueOf("white");
    private static final Paint HIGHLIGHT_COLOR = Paint.valueOf("#7289da");

    @FXML private AnchorPane parent; // parent anchor pane of the scene

    @FXML private JFXButton startButton;
    @FXML private JFXButton settingsButton; // the three dot button
    @FXML private JFXButton generateQRCodeButton;

    @FXML private VBox settingsMenu; // Vbox containing server connection options
    @FXML private JFXButton menuWiFiButton;
    @FXML private JFXButton menuBluetoothButton;

    @FXML private Label statusLabel; // label for server status (online/offline)
    @FXML private Label connectedDeviceLabel; // name of connected device (otherwise "Not Connected")
    @FXML private Label connectionTypeLabel; // label for server connection type (WiFi/Bluetooth)

    // extra fields whose values depend on which connection mode is selected
    @FXML private Label field1;
    @FXML private Label field2;
    @FXML private Label field1value;
    @FXML private Label field2value;

    private Server server;
    private ConnectionType serverType = ConnectionType.WIFI;
    private boolean menuIsShowing = false;

    private JFrame qrCodeWindow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initializes server related fields
        stopServer();

        // initializes client related fields
        onClientDisconnect();

        // select server in WiFi mode
        menuWiFiAction(new ActionEvent());

        // keep menu hidden on start
        hideMenu();

        parent.setOnMouseClicked(this::onMouseClick);
    }

    private void onMouseClick(MouseEvent event) {
        // if user clicks on any place other than the settings button
        // the menu is hidden
        if (menuIsShowing) hideMenu();
    }

    @FXML
    private void settingsButtonAction(ActionEvent event) {
        if (!menuIsShowing) showMenu();
        else hideMenu();
    }

    /**
     * onAction for the WiFi menu item in settingsMenu
     */
    @FXML
    private void menuWiFiAction(ActionEvent event) {
        if (server != null && server.isRunning()) {
            new Alert(Alert.AlertType.WARNING, "Stop server first").show();
        } else {
            serverType = ConnectionType.WIFI;

            clearMenuSelect(); // clear previous selection highlight
            menuWiFiButton.setTextFill(HIGHLIGHT_COLOR); // highlight the WiFi button

            connectionTypeLabel.setText(serverType.toString());

            // set extra field values
            field1.setText("IP Address");
            field2.setText("Port");
            field1value.setText(getLocalIpAddress());
            field2value.setText("-");
        }
        hideMenu();
    }

    /**
     * onAction for the bluetooth menu item in settingsMenu
     */
    @FXML
    private void menuBluetoothAction(ActionEvent event) {
        if (server != null && server.isRunning()) {
            new Alert(Alert.AlertType.WARNING, "Stop server first").show();
        } else if (!BluetoothServer.isBluetoothEnabled()) {
            new Alert(Alert.AlertType.ERROR, "Bluetooth is not enabled").show();
        } else {
            serverType = ConnectionType.BLUETOOTH;

            clearMenuSelect(); // clear previous selection highlight
            menuBluetoothButton.setTextFill(HIGHLIGHT_COLOR); // highlight the bluetooth button

            connectionTypeLabel.setText(serverType.toString());

            // set extra field values
            field1.setText("Bluetooth Name");
            field2.setText("Address");
            try {
                field1value.setText(BluetoothServer.getFriendlyName());
            } catch (BluetoothStateException e) {
                e.printStackTrace();
            }
            field2value.setText("-");
        }
        hideMenu();
    }

    /**
     * show the server connection type selection menu
     */
    private void showMenu() {
        settingsMenu.setVisible(true);
        settingsMenu.setDisable(false);
        menuIsShowing = true;
    }

    /**
     * hide the server connection type selection menu
     */
    private void hideMenu() {
        settingsMenu.setVisible(false);
        settingsMenu.setDisable(true);
        menuIsShowing = false;
    }

    /**
     * clear any text previous color change in menu
     */
    private void clearMenuSelect() {
        for (Node i: settingsMenu.getChildren()) {
            if (i instanceof JFXButton) {
                ((JFXButton) i).setTextFill(BASE_COLOR);
            }
        }
    }

    @FXML
    private void startButtonAction(ActionEvent event) {
        if (server == null || !server.isRunning()) {
            setupServer();
            startServer();
        } else {
            stopServer();
        }
    }

    /**
     * Initialize the server variable
     * (according to serverType)
     */
    private void startServer() {
        assert server != null;
        try {
            server.start();

            statusLabel.setText(STATUS_LABEL_ONLINE);
            statusLabel.setTextFill(CONNECTED_COLOR);
            startButton.setText(STOP_BUTTON_TEXT);

            // set field2 value
            switch (serverType) {
                case WIFI:
                    WiFiServer wiFiServer = (WiFiServer) server;
                    field2value.setText(String.valueOf(wiFiServer.getLocalPort()));
                    break;
                case BLUETOOTH:
                    BluetoothServer bluetoothServer = (BluetoothServer) server;
                    field2value.setText(bluetoothServer.getBluetoothAddress());
                    break;
                default:
                    LOGGER.log(Level.SEVERE, "HomeScreenViewController.startButtonAction: invalid server type");
            }

            generateQRCodeButton.setDisable(false);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getLocalizedMessage()).show();
            e.printStackTrace();
        }
    }

    private void stopServer() {
        try {
            if (server != null) server.stop();

            statusLabel.setText(STATUS_LABEL_OFFLINE);
            statusLabel.setTextFill(BASE_COLOR);
            startButton.setText(START_BUTTON_TEXT);
            field2value.setText("-");

            generateQRCodeButton.setDisable(true);
            closeQRCodeWindow();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getLocalizedMessage()).show();
            e.printStackTrace();
        }
    }

    private void setupServer() {
        switch (serverType) {
            case WIFI:
                server = new WiFiServer() {
                    @Override
                    public void onConnect(String deviceName) {
                        Platform.runLater(() -> onClientConnect(deviceName));
                    }

                    @Override
                    public void onDisconnect() {
                        Platform.runLater(() -> onClientDisconnect());
                    }

                    @Override
                    public void onError(@Nullable Exception exception) {
                        String error;
                        if (exception != null && (error = exception.getLocalizedMessage()) != null) {
                            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, error).show());
                        }
                    }
                };
                break;
            case BLUETOOTH:
                server = new BluetoothServer() {
                    @Override
                    public void onConnect(String deviceName) {
                        Platform.runLater(() -> onClientConnect(deviceName));
                    }

                    @Override
                    public void onDisconnect() {
                        Platform.runLater(() -> onClientDisconnect());
                    }

                    @Override
                    public void onError(@Nullable Exception exception) {
                        String error;
                        if (exception != null && (error = exception.getLocalizedMessage()) != null) {
                            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, error).show());
                        }
                    }
                };
                break;
            default:
                LOGGER.log(Level.SEVERE, "HomeScreenViewController.startButtonAction: invalid server type");
        }
    }

    /**
     * called when a client connects to server
     * @param deviceName device name of client
     */
    private void onClientConnect(String deviceName) {
        connectedDeviceLabel.setText(deviceName);
        connectedDeviceLabel.setTextFill(CONNECTED_COLOR);
        closeQRCodeWindow();
    }

    /**
     * called when a client disconnects from server
     */
    private void onClientDisconnect() {
        connectedDeviceLabel.setText(NOT_CONNECTED_TEXT);
        connectedDeviceLabel.setTextFill(BASE_COLOR);
        closeQRCodeWindow();
    }

    @FXML
    private void generateQRCodeAction(ActionEvent event) {
        new Thread(() -> {
            closeQRCodeWindow();
            showQRCodeWindow();
        }).start();
    }

    private synchronized void showQRCodeWindow() {
        qrCodeWindow = showQRCode(server.getQRCodeInfo(), 500, 500);
    }

    private synchronized void closeQRCodeWindow() {
        if (qrCodeWindow != null) {
            qrCodeWindow.dispose();
            qrCodeWindow = null;
        }
    }

    public static Parent getRoot() throws IOException {
        return FXMLLoader.load(HomeScreenViewController.class.getResource("/fxml/HomeScreenView.fxml"));
    }
}




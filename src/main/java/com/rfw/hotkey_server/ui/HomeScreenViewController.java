package com.rfw.hotkey_server.ui;

import com.jfoenix.controls.JFXButton;
import com.rfw.hotkey_server.net.ConnectionType;
import com.rfw.hotkey_server.net.Server;
import com.rfw.hotkey_server.net.WiFiServer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static com.rfw.hotkey_server.util.Utils.getLocalIpAddress;
import static com.rfw.hotkey_server.util.Utils.showQRCode;

public class HomeScreenViewController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(HomeScreenViewController.class.getName());

    private static final String CONNECTED_STYLE = "-fx-text-fill: #7FFF00;";
    private static final String NOT_CONNECTED_STYLE = "-fx-text-fill: white;";

    @FXML
    private JFXButton settingButtonID;
    @FXML
    private JFXButton startButtonID;

    @FXML
    private Label statusLabelID;
    @FXML
    private Label connectedDeviceLabelID;
    @FXML
    private Label iPLabelID;
    @FXML
    private Label portLabelID;
    @FXML
    private Label connectionTypeLabelID;

    private Server server;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server = new WiFiServer() {
            @Override
            public void onConnect(String deviceName) {
                ConnectionType type = getConnectionType();
                Platform.runLater(() -> {
                    connectedDeviceLabelID.setText(deviceName);
                    connectedDeviceLabelID.setStyle(CONNECTED_STYLE);
                    connectionTypeLabelID.setText(type.toString());
                });
            }

            @Override
            public void onDisconnect() {
                Platform.runLater(() -> {
                    connectedDeviceLabelID.setText("Not Connected");
                    connectedDeviceLabelID.setStyle(NOT_CONNECTED_STYLE);
                    connectionTypeLabelID.setText("-");
                });
            }
        };
        statusLabelID.setText("Offline");
        statusLabelID.setStyle(NOT_CONNECTED_STYLE);
        startButtonID.setText("Start");

        iPLabelID.setText(getLocalIpAddress());
        portLabelID.setText("-");

        connectedDeviceLabelID.setText("Not Connected");
        connectedDeviceLabelID.setStyle(NOT_CONNECTED_STYLE);
        connectionTypeLabelID.setText("-");
    }

    @FXML
    private void startButtonAction(ActionEvent event) {
        if (!server.isRunning()) {
            try {
                server.start();
                statusLabelID.setText("Online");
                statusLabelID.setStyle(CONNECTED_STYLE);
                startButtonID.setText("Stop");
                portLabelID.setText(String.valueOf(((WiFiServer) server).getLocalPort()));
            } catch (IOException e) {
                // TODO: implement error dialog
                e.printStackTrace();
            }
        } else {
            try {
                server.stop();
                statusLabelID.setText("Offline");
                statusLabelID.setStyle(NOT_CONNECTED_STYLE);
                startButtonID.setText("Start");
                portLabelID.setText("-");
            } catch (IOException e) {
                // TODO: implement error dialog
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void generateQRCodeAction(ActionEvent event) {
        new Thread(() -> showQRCode(server.getQRCodeInfo(), 500, 500)).start();
    }

    public static Parent getRoot() throws IOException {
        return FXMLLoader.load(HomeScreenViewController.class.getResource("/fxml/HomeScreenView.fxml"));
    }
}




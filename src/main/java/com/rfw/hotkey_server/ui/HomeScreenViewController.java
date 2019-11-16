package com.rfw.hotkey_server.ui;

import com.jfoenix.controls.JFXButton;
import com.rfw.hotkey_server.net.ConnectionType;
import com.rfw.hotkey_server.net.Server;
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

    public static Parent getRoot() throws IOException {
        return FXMLLoader.load(HomeScreenViewController.class.getResource("/fxml/HomeScreenView.fxml"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server = new Server() {
            @Override
            protected void onConnect(String deviceName, ConnectionType type) {
                Platform.runLater(() -> {
                    connectedDeviceLabelID.setText(deviceName);
                    connectedDeviceLabelID.setStyle(CONNECTED_STYLE);
                    connectionTypeLabelID.setText(type == ConnectionType.WIFI ? "WiFi" : "Unknown");
                });
            }

            @Override
            protected void onDisconnect() {
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

    /**
     * A method to start server
     *
     * @param event
     */

    @FXML
    private void startButtonAction(ActionEvent event) {
        if (!server.isRunning()) {
            try {
                server.start();
                statusLabelID.setText("Online");
                statusLabelID.setStyle(CONNECTED_STYLE);
                startButtonID.setText("Stop");
                portLabelID.setText(String.valueOf(server.getLocalPort()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            server.stop();
            statusLabelID.setText("Offline");
            statusLabelID.setStyle(NOT_CONNECTED_STYLE);
            startButtonID.setText("Start");
            portLabelID.setText("-");
        }
    }

    @FXML
    private void generateQRCodeAction(ActionEvent event) {
        new Thread(() -> {
            ByteArrayOutputStream stream =
                    QRCode.from(server.getQRCodeInfo())
                            .to(ImageType.JPG)
                            .withSize(500, 500)
                            .stream();
            byte[] bytes = stream.toByteArray();
            ImageIcon icon = new ImageIcon(bytes);

            JFrame frame = new JFrame();
            frame.setTitle("Scan this");
            JLabel label = new JLabel(icon);
            frame.add(label);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }).start();
    }
}




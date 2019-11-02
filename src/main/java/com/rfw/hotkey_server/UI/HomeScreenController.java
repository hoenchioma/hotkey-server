package com.rfw.hotkey_server.UI;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeScreenController implements Initializable {
    @FXML
    private JFXButton settingButtonID;

    @FXML
    private Label statusLebelID;

    @FXML
    private Label connectedDeviceLebelID;

    @FXML
    private Label IPLebelID;

    @FXML
    private Label portLebelID;

    @FXML
    private Label connectionTypeLebelID;

    @FXML
    private JFXButton startButtonID;

    private  Boolean isServerOnline;

//    public static Pane getRoot() throws IOException {
//        Parent root = FXMLLoader.load(HomeScreenController.class.getResource("HomeScreenView.fxml"));
//        return (Pane) root;
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isServerOnline = false;
    }

    @FXML
    public void startButtonAction(ActionEvent event){
        // TODO Run Server
        //  change  text
        String status = null,device = null,IP = null,port = null,connectionType = null;

        statusLebelID.setText(status);
        connectedDeviceLebelID.setText(device);
        IPLebelID.setText(IP);
        portLebelID.setText(port);
        connectionTypeLebelID.setText(connectionType);


        if(!isServerOnline) {
            startButtonID.setText("Stop");
            isServerOnline = true;
        }
        else {
            startButtonID.setText("Start");
            isServerOnline = false;
        }

    }
}




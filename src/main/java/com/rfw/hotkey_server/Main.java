package com.rfw.hotkey_server;

import com.rfw.hotkey_server.ui.HomeScreenViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = HomeScreenViewController.getRoot();

        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle("Hotkey Server");
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setScene(scene);
        stage.show();
//        stage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

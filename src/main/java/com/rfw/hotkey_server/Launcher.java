package com.rfw.hotkey_server;

import com.rfw.hotkey_server.ui.HomeScreenViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Launches the JavaFx application of the Server GUI
 *
 * @author Raheeb Hassan
 */
public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = HomeScreenViewController.getRoot();

        Image icon = new Image(getClass().getResourceAsStream("/images/hotkey-icon-128x128.png"));

        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle("Hotkey Server");
        stage.setOnCloseRequest(t -> { // force close application when close is pressed
            Platform.exit();
            System.exit(0);
        });
        stage.setScene(scene);
        stage.getIcons().add(icon);
        stage.show();
//        stage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

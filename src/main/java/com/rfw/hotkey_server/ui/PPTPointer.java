package com.rfw.hotkey_server.ui;

// Java program to create circle by passing the
// coordinates of the center and radius
// as arguments in constructor
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.scene.Group;
import javafx.stage.StageStyle;

public class PPTPointer extends Application {

    // launch the application
    private Circle circle;
    public void start(Stage stage)
    {
        // set title for the stage
        //Pane pane = new Pane();
        stage.setTitle("creating circle");

        // create a circle
        circle = new Circle(150.0f, 150.0f, 6.f);
        circle.setFill(Color.RED);
        // create a Group
        Group group = new Group(circle);

        // create a scene
        Scene scene = new Scene(group, 500, 300);
        //pane.getChildren().addAll(circle);
        stage.setMaximized(true);
        circle.setCenterX(100.0f);
        circle.setCenterY(700.0f);
        // set the scene
        stage.setScene(scene);
        //stage.setFullScreen(true);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

    public static void main(String args[])
    {
        // launch the application
        launch(args);
    }

    public void movePointer(float x,float y){
        circle.setCenterX(x);
        circle.setCenterY(y);
    }
}
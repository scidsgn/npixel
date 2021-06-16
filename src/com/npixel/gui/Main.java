package com.npixel.gui;

import com.npixel.base.Document;
import com.npixel.base.DocumentEvent;
import com.npixel.io.DocumentWriter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        AppView root = new AppView(primaryStage);

        Scene scene = new Scene(root, 1500, 940);
        scene.getStylesheets().add(getClass().getResource("styles.css").toString());

        primaryStage.setTitle("NPIXEL");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

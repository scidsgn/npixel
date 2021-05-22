package com.npixel.gui;

import com.npixel.base.Document;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox();

        Document doc = new Document(300, 200);
        doc.initNewDocument();

        DocumentView docView = new DocumentView(doc);
        root.getChildren().add(docView);

        HBox.setHgrow(docView, Priority.ALWAYS);

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

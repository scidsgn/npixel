package com.npixel.gui;

import com.npixel.base.Document;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class Main extends Application {
    private TabPane tabPane;

    private Tab getDocumentTab(Document doc) {
        Tab tab = new Tab(doc.getShortName());
        tab.setClosable(false);

        tab.setContent(new DocumentView(doc));

        return tab;
    }

    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox();

        tabPane = new TabPane();
        HBox.setHgrow(tabPane, Priority.ALWAYS);

        Document doc = new Document(null);
        doc.initNewDocument();
        tabPane.getTabs().add(getDocumentTab(doc));

        root.getChildren().add(tabPane);

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

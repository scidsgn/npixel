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
    private TabPane tabPane;

    private Tab getDocumentTab(Document doc) {
        Tab tab = new Tab(doc.getTabName());
        tab.setClosable(false);

        tab.setContent(new DocumentView(doc));

        doc.on(DocumentEvent.NAMEUPDATED, d -> {
            tab.setText(doc.getTabName());
            return null;
        });

        return tab;
    }

    @Override
    public void start(Stage primaryStage) {
        AppView root = new AppView(primaryStage);

        tabPane = new TabPane();
        HBox.setHgrow(tabPane, Priority.ALWAYS);

        Button testSave = new Button("test save");
        testSave.setOnAction(actionEvent -> {
            // TEST
            try {
                File file = new File("test.npxl");
                file.createNewFile();
                FileOutputStream fileOS = new FileOutputStream(file, false);
                DataOutputStream stream = new DataOutputStream(fileOS);

                DocumentWriter documentWriter = new DocumentWriter(stream);
                //documentWriter.writeDocument(doc);

                stream.flush();
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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

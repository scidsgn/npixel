package com.npixel.gui;

import com.npixel.base.Document;
import com.npixel.base.DocumentEvent;
import com.npixel.gui.icons.Icons;
import com.npixel.io.DocumentReader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppView extends VBox {
    private final Stage stage;

    private final ToolBar appBar;
    private final TabPane tabPane;

    public AppView(Stage stage) {
        this.stage = stage;

        appBar = new ToolBar();
        tabPane = new TabPane();

        prepareLayout();
    }

    private void prepareLayout() {
        Button newDoc = new Button("", Icons.getImageView("filenew", 24));
        newDoc.setTooltip(new Tooltip("New document"));
        newDoc.setOnAction(event -> createNewDocument());

        Button openDoc = new Button("", Icons.getImageView("fileopen", 24));
        openDoc.setTooltip(new Tooltip("Open document"));
        openDoc.setOnAction(event -> openDocument());

        Button saveDoc = new Button("", Icons.getImageView("filesave", 24));
        saveDoc.setTooltip(new Tooltip("Save document"));
        saveDoc.setOnAction(event -> saveDocument());

        appBar.getItems().addAll(newDoc, openDoc, saveDoc);

        VBox.setVgrow(tabPane, Priority.ALWAYS);
        getChildren().addAll(appBar, tabPane);
    }

    private Tab getDocumentTab(Document doc) {
        Tab tab = new Tab(doc.getTabName());

        tab.setContent(new DocumentView(doc));

        doc.on(DocumentEvent.VIEWPORTSCALEUPDATED, d -> {
            tab.setText(doc.getTabName());
            return null;
        });

        return tab;
    }

    private void createNewDocument() {
        Document doc = new Document(null);
        doc.initNewDocument();
        addTab(doc);
    }

    private void addTab(Document doc) {
        Tab tab = getDocumentTab(doc);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    private void openDocument() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open NPIXEL Document");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("NPIXEL Documents", "*.npxl")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                Document doc = new Document(selectedFile.getAbsolutePath());

                FileInputStream fileIS = new FileInputStream(selectedFile);
                DataInputStream stream = new DataInputStream(fileIS);

                DocumentReader documentReader = new DocumentReader(stream);
                documentReader.readDocument(doc);

                stream.close();
                fileIS.close();

                addTab(doc);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't open the specified document.");
                alert.showAndWait();
            }
        }
    }

    private void saveDocument() {

    }
}

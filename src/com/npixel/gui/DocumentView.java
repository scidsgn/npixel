package com.npixel.gui;

import com.npixel.base.Document;
import com.npixel.gui.nodeeditor.NodeEditorPanel;
import com.npixel.gui.rastereditor.RasterEditorPanel;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DocumentView extends VBox {
    private Document doc;

    private SplitPane splitPane;
    private RasterEditorPanel rasterEditorPanel;
    private NodeEditorPanel nodeEditorPanel;

    public DocumentView(Document doc) {
        this.doc = doc;

        prepareLayout();
    }

    private void prepareLayout() {
        splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        rasterEditorPanel = new RasterEditorPanel(doc);
        splitPane.getItems().add(rasterEditorPanel);

        nodeEditorPanel = new NodeEditorPanel(doc.getTree());
        splitPane.getItems().add(nodeEditorPanel);

        getChildren().add(splitPane);
    }
}

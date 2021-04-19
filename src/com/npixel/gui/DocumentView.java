package com.npixel.gui;

import com.npixel.base.Document;
import com.npixel.gui.nodeeditor.NodeEditorPanel;
import com.npixel.gui.nodeeditor.NodePropertiesPanel;
import com.npixel.gui.rastereditor.RasterEditorPanel;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DocumentView extends VBox {
    private Document doc;

    private RasterEditorPanel rasterEditorPanel;
    private NodeEditorPanel nodeEditorPanel;
    private NodePropertiesPanel nodePropertiesPanel;

    public DocumentView(Document doc) {
        this.doc = doc;

        prepareLayout();
    }

    private void prepareLayout() {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        SplitPane splitPane2 = new SplitPane();
        splitPane2.setOrientation(Orientation.VERTICAL);

        rasterEditorPanel = new RasterEditorPanel(doc);
        nodeEditorPanel = new NodeEditorPanel(doc.getTree());
        splitPane2.getItems().addAll(rasterEditorPanel, nodeEditorPanel);

        nodePropertiesPanel = new NodePropertiesPanel(doc.getTree());

        splitPane.getItems().addAll(splitPane2, nodePropertiesPanel);

        getChildren().add(splitPane);
    }
}

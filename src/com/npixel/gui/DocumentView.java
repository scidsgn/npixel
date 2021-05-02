package com.npixel.gui;

import com.npixel.base.Document;
import com.npixel.gui.nodeeditor.NodeEditorPanel;
import com.npixel.gui.sidepanel.DocumentSidePanel;
import com.npixel.gui.sidepanel.NodePropertiesPanel;
import com.npixel.gui.sidepanel.ToolPropertiesPanel;
import com.npixel.gui.rastereditor.RasterEditorPanel;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DocumentView extends VBox {
    private Document doc;


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

        RasterEditorPanel rasterEditorPanel = new RasterEditorPanel(doc);
        NodeEditorPanel nodeEditorPanel = new NodeEditorPanel(doc.getTree());
        splitPane2.getItems().addAll(rasterEditorPanel, nodeEditorPanel);

        DocumentSidePanel sidePanel = new DocumentSidePanel(doc);

        splitPane.getItems().addAll(splitPane2, sidePanel);

        getChildren().add(splitPane);
    }
}

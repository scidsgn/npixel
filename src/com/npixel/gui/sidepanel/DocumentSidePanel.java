package com.npixel.gui.sidepanel;

import com.npixel.base.Document;
import com.npixel.base.tool.ITool;
import com.npixel.base.tree.NodeTreeEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class DocumentSidePanel extends ScrollPane {
    private final Document doc;

    public DocumentSidePanel(Document doc) {
        this.doc = doc;

        prepareLayout();
    }

    private void prepareLayout() {
        VBox vbox = new VBox();
        vbox.getStyleClass().add("document-side-panel");

        TitledPane colorPane = new TitledPane("Color", new ColorPanel(doc));
        TitledPane swatchPane = new TitledPane("Palettes", new PalettePanel(doc));
        TitledPane toolPane = new TitledPane("Tool", new ToolPropertiesPanel(doc.getTree()));
        TitledPane brushPane = new TitledPane("Brush", new Label("todo"));
        TitledPane nodePane = new TitledPane("Node", new NodePropertiesPanel(doc.getTree()));

        doc.getTree().on(NodeTreeEvent.NODETOOLCHANGED, node -> {
            ITool tool = node.getActiveTool();
            if (tool != null) {
                toolPane.setText(tool.getName() + " tool");
            } else {
                toolPane.setText("Tool");
            }

            return null;
        });
        doc.getTree().on(NodeTreeEvent.ACTIVENODECHANGED, node -> {
            if (node == null) {
                return null;
            }

            ITool tool = node.getActiveTool();
            if (tool != null) {
                toolPane.setText(tool.getName() + " tool");
            } else {
                toolPane.setText("Tool");
            }

            return null;
        });

        vbox.getChildren().addAll(colorPane, swatchPane, toolPane, brushPane, nodePane);

        setContent(vbox);

        setMinWidth(300);
        setMaxWidth(300);
        setFitToWidth(true);
        setFitToHeight(true);
    }
}

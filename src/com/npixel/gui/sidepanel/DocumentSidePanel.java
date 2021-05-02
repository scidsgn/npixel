package com.npixel.gui.sidepanel;

import com.npixel.base.Document;
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

        vbox.getChildren().addAll(
                new TitledPane("Tool Properties", new ToolPropertiesPanel(doc.getTree())),
                new TitledPane("Node Properties", new NodePropertiesPanel(doc.getTree()))
        );

        setContent(vbox);

        minWidthProperty().setValue(300);
        maxWidthProperty().setValue(300);
        fitToWidthProperty().set(true);
    }
}

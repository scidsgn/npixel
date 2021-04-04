package com.npixel.gui.rastereditor;

import com.npixel.base.Document;
import com.npixel.base.tree.NodeTreeEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class RasterEditorPanel extends HBox {
    Document doc;

    RasterEditor rasterEditor;

    public RasterEditorPanel(Document doc) {
        this.doc = doc;

        prepareLayout();
        prepareEvents();
    }

    private void prepareLayout() {
        rasterEditor = new RasterEditor();
        HBox.setHgrow(rasterEditor, Priority.ALWAYS);

        getChildren().add(rasterEditor);
    }

    private void prepareEvents() {
        doc.getTree().on(NodeTreeEvent.ACTIVENODECHANGED, node -> {
            rasterEditor.setCurrentNode(node);
            return null;
        });
    }
}

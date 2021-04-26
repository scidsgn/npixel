package com.npixel.gui.rastereditor;

import com.npixel.base.Document;
import com.npixel.base.tool.ITool;
import com.npixel.base.tree.NodeTreeEvent;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class RasterEditorPanel extends HBox {
    Document doc;

    RasterEditor rasterEditor;
    ToolBar toolBar;

    public RasterEditorPanel(Document doc) {
        this.doc = doc;

        prepareLayout();
        prepareEvents();
    }

    private void prepareLayout() {
        toolBar = new ToolBar();
        toolBar.setOrientation(Orientation.VERTICAL);

        rasterEditor = new RasterEditor();
        HBox.setHgrow(rasterEditor, Priority.ALWAYS);

        getChildren().addAll(toolBar, rasterEditor);
    }

    private void prepareEvents() {
        doc.getTree().on(NodeTreeEvent.ACTIVENODECHANGED, node -> {
            toolBar.getItems().clear();

            if (node != null) {
                for (ITool tool : node.getTools()) {
                    Button toolButton = new Button(tool.getName());

                    toolButton.setOnAction(event -> node.setActiveTool(tool));

                    toolBar.getItems().add(toolButton);
                }
            }

            rasterEditor.setCurrentNode(node);
            return null;
        });
    }
}

package com.npixel.gui.nodeeditor;

import com.npixel.base.tree.NodeTree;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class NodeEditorPanel extends VBox {
    NodeTree tree;
    NodeEditor nodeEditor;
    ToolBar toolBar;

    public NodeEditorPanel(NodeTree tree) {
        this.tree = tree;

        prepareLayout();
    }

    private void prepareLayout() {
        toolBar = new ToolBar();
        prepareToolbar();

        nodeEditor = new NodeEditor(tree);
        VBox.setVgrow(nodeEditor, Priority.ALWAYS);

        getChildren().addAll(toolBar, nodeEditor);
    }

    private void prepareToolbar() {
        Button renderBtn = new Button("Process node");

        renderBtn.setOnAction(event -> {
            if (tree.getActiveNode() != null) {
                tree.getActiveNode().process();
            }
        });

        toolBar.getItems().add(renderBtn);
    }
}

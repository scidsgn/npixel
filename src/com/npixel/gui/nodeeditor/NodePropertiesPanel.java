package com.npixel.gui.nodeeditor;

import com.npixel.base.tree.NodeTree;
import com.npixel.base.tree.NodeTreeEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class NodePropertiesPanel extends VBox {
    private final NodeTree tree;

    public NodePropertiesPanel(NodeTree tree) {
        this.tree = tree;

        prepareLayout();
    }

    private void prepareLayout() {
        HBox nameBox = new HBox();
        Label nameLabel = new Label("Name:");
        TextField nodeName = new TextField();
        nodeName.setDisable(true);

        nodeName.textProperty().addListener((observable, oldValue, newValue) -> {
            tree.getActiveNode().setName(newValue);
        });

        nameBox.getChildren().addAll(nameLabel, nodeName);

        tree.on(NodeTreeEvent.ACTIVENODECHANGED, node -> {
            if (node != null) {
                nodeName.setText(node.getName());
                nodeName.setDisable(false);
            } else {
                nodeName.setDisable(true);
            }

            return null;
        });

        getChildren().addAll(nameBox, nodeName);
        maxWidthProperty().setValue(300);
    }
}

package com.npixel.gui.nodeeditor;

import com.npixel.base.node.Node;
import com.npixel.base.properties.*;
import com.npixel.base.tree.NodeTree;
import com.npixel.base.tree.NodeTreeEvent;
import com.npixel.gui.properties.DoublePropertyEditor;
import com.npixel.gui.properties.IntPropertyEditor;
import com.npixel.gui.properties.OptionPropertyEditor;
import com.npixel.gui.properties.PropertiesEditor;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
        HBox.setHgrow(nodeName, Priority.ALWAYS);
        nodeName.setDisable(true);

        nodeName.textProperty().addListener((observable, oldValue, newValue) -> {
            tree.getActiveNode().setName(newValue);
        });

        nameBox.getChildren().addAll(nameLabel, nodeName);

        PropertiesEditor propertiesEditor = new PropertiesEditor();

        tree.on(NodeTreeEvent.ACTIVENODECHANGED, node -> {
            propertiesEditor.setTarget(node);

            if (node != null) {
                nodeName.setText(node.getName());
                nodeName.setDisable(false);
            } else {
                nodeName.setDisable(true);
            }

            return null;
        });

        getChildren().addAll(nameBox, propertiesEditor);
        maxWidthProperty().setValue(300);
    }
}

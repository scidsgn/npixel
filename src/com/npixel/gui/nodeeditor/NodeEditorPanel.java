package com.npixel.gui.nodeeditor;

import com.npixel.base.tree.NodeTree;
import com.npixel.nodelibrary.NodeLibrary;
import com.npixel.nodelibrary.NodeLibraryNode;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class NodeEditorPanel extends HBox {
    NodeTree tree;
    NodeEditor nodeEditor;
    ToolBar toolBar;

    public NodeEditorPanel(NodeTree tree) {
        this.tree = tree;

        prepareLayout();
    }

    private void prepareLayout() {
        toolBar = new ToolBar();
        toolBar.setOrientation(Orientation.VERTICAL);
        prepareToolbar();

        nodeEditor = new NodeEditor(tree);
        HBox.setHgrow(nodeEditor, Priority.ALWAYS);

        getChildren().addAll(toolBar, nodeEditor);
    }

    private Button createAddNodeButton(String nodeId) {
        NodeLibraryNode node = NodeLibrary.nodeLibrary.getNode(nodeId);

        ImageView nodeIcon = new ImageView(node.getIcon());
        nodeIcon.setFitWidth(24);
        nodeIcon.setFitHeight(24);

        Button nodeBtn = new Button("", nodeIcon);
        nodeBtn.setTooltip(new Tooltip("Add " + node.getName()));

        nodeBtn.setOnAction(event -> {
            nodeEditor.addNode(node.create(tree));
        });

        return nodeBtn;
    }

    private void prepareToolbar() {
        toolBar.getItems().addAll(
                createAddNodeButton("ShapeRectangle"),
                createAddNodeButton("ShapeEllipse"),
                new Separator(),
                createAddNodeButton("CompAComp"),
                createAddNodeButton("CompMask")
        );
    }
}

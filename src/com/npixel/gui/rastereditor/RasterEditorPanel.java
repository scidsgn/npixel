package com.npixel.gui.rastereditor;

import com.npixel.base.Document;
import com.npixel.base.node.Node;
import com.npixel.base.tool.ITool;
import com.npixel.base.tree.NodeTreeEvent;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
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

        rasterEditor = new RasterEditor(doc);
        HBox.setHgrow(rasterEditor, Priority.ALWAYS);

        getChildren().addAll(toolBar, rasterEditor);
    }

    private Button createToolButton(Node node, ITool tool) {
        ImageView toolIcon = new ImageView(tool.getIcon());
        toolIcon.setFitWidth(24);
        toolIcon.setFitHeight(24);

        Button toolButton = new Button("", toolIcon);
        toolButton.setTooltip(new Tooltip(tool.getName()));
        if (tool == node.getActiveTool()) {
            toolButton.getStyleClass().add("active-tool");
        }

        toolButton.setOnAction(event -> {
            for (javafx.scene.Node n : toolBar.getItems()) {
                if (n.getStyleClass().contains("active-tool")) {
                    n.getStyleClass().remove("active-tool");
                    break;
                }
            }

            node.setActiveTool(tool);
            toolButton.getStyleClass().add("active-tool");
        });

        return toolButton;
    }

    private void setActiveNode(Node node) {
        toolBar.getItems().clear();

        if (node != null) {
            if (node.getActiveTool() == null) {
                node.setActiveTool(rasterEditor.getHandTool());
            }
            toolBar.getItems().add(createToolButton(node, rasterEditor.getHandTool()));

            for (ITool tool : node.getTools()) {
                toolBar.getItems().add(createToolButton(node, tool));
            }

            rasterEditor.setCurrentNode(node);
        } else {
            rasterEditor.setCurrentNode(null);
        }
    }

    private void prepareEvents() {
        doc.getTree().on(NodeTreeEvent.ACTIVENODECHANGED, node -> {
            setActiveNode(node);
            return null;
        });
    }
}

package com.npixel.gui.sidepanel;

import com.npixel.base.tool.ITool;
import com.npixel.base.tree.NodeTree;
import com.npixel.base.tree.NodeTreeEvent;
import com.npixel.gui.properties.PropertiesEditor;
import javafx.scene.layout.VBox;

public class ToolPropertiesPanel extends VBox {
    private final NodeTree tree;

    public ToolPropertiesPanel(NodeTree tree) {
        this.tree = tree;

        prepareLayout();
    }

    private void prepareLayout() {
        PropertiesEditor editor = new PropertiesEditor();

        tree.on(NodeTreeEvent.NODETOOLCHANGED, node -> {
            editor.setTarget(node.getActiveTool());
            return null;
        });

        tree.on(NodeTreeEvent.ACTIVENODECHANGED, node -> {
            if (node != null) {
                ITool tool = node.getActiveTool();
                editor.setTarget(tool);
            } else {
                editor.setTarget(null);
            }

            return null;
        });

        getChildren().addAll(editor);
    }

}

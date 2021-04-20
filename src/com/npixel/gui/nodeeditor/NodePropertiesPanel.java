package com.npixel.gui.nodeeditor;

import com.npixel.base.node.Node;
import com.npixel.base.node.properties.INodeProperty;
import com.npixel.base.node.properties.IntNodeProperty;
import com.npixel.base.node.properties.NodePropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.base.tree.NodeTreeEvent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class NodePropertiesPanel extends VBox {
    private final NodeTree tree;

    private final VBox panelContainer = new VBox();

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

        tree.on(NodeTreeEvent.ACTIVENODECHANGED, node -> {
            if (node != null) {
                nodeName.setText(node.getName());
                nodeName.setDisable(false);

                panelContainer.getChildren().clear();
                populatePropertyPanel(node);
            } else {
                nodeName.setDisable(true);
                panelContainer.getChildren().clear();
            }

            return null;
        });

        getChildren().addAll(nameBox, panelContainer);
        maxWidthProperty().setValue(300);
    }

    private void populatePropertyPanel(Node node) {
        for (NodePropertyGroup propertyGroup : node.getPropertyGroups()) {
            VBox content = new VBox();

            for (INodeProperty property : propertyGroup.getProperties()) {
                VBox propPanel = new VBox();

                Label propLabel = new Label(property.getName());
                javafx.scene.Node propControl = createPropertyControl(property);

                propPanel.getChildren().addAll(propLabel, propControl);

                content.getChildren().add(propPanel);
            }

            TitledPane pane = new TitledPane(propertyGroup.getName(), content);
            pane.setExpanded(true);

            panelContainer.getChildren().add(pane);
        }
    }

    private javafx.scene.Node createPropertyControl(INodeProperty property) {
        // TODO: make it better than this
        if (property instanceof IntNodeProperty) {
            IntNodeProperty p = (IntNodeProperty)property;

            Slider slider = new Slider(p.getMinValue(), p.getMaxValue(), p.getValue());
            slider.setMajorTickUnit(1);
            slider.setMinorTickCount(0);
            slider.setShowTickMarks(true);
            slider.setSnapToTicks(true);

            slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                p.setValue(newValue.intValue());
            });

            return slider;
        }

        return null;
    }
}

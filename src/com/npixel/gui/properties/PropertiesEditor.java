package com.npixel.gui.properties;

import com.npixel.base.properties.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PropertiesEditor extends VBox {
    private IUpdateable target = null;

    public PropertiesEditor() {
        prepareLayout();
    }

    private void prepareLayout() {
        getChildren().clear();

        if (target == null) {
            Label label = new Label("Nothing selected");
            label.getStyleClass().add("no-editor-context");
            getChildren().add(label);

            return;
        }

        for (PropertyGroup propertyGroup : target.getPropertyGroups()) {
            VBox content = new VBox();

            for (IProperty property : propertyGroup.getProperties()) {
                if (property instanceof BooleanProperty) {
                    content.getChildren().add(new BooleanPropertyEditor((BooleanProperty)property));
                } else {
                    Pane propPanel;
                    if (property.isCompact()) {
                        propPanel = new HBox();
                    } else {
                        propPanel = new VBox();
                    }

                    Label propLabel = new Label(property.getName());
                    javafx.scene.Node propControl = createPropertyControl(property);

                    propPanel.getChildren().addAll(propLabel, propControl);

                    content.getChildren().add(propPanel);
                }
            }

            TitledPane pane = new TitledPane(propertyGroup.getName().toUpperCase(), content);
            pane.getStyleClass().add("property-group");
            pane.setCollapsible(false);
            pane.setExpanded(true);

            getChildren().add(pane);
        }
    }

    private Node createPropertyControl(IProperty property) {
        // TODO: make it better than this
        if (property instanceof IntProperty) {
            return new IntPropertyEditor((IntProperty)property);
        } else if (property instanceof DoubleProperty) {
            return new DoublePropertyEditor((DoubleProperty)property);
        } else if (property instanceof OptionProperty) {
            return new OptionPropertyEditor((OptionProperty)property);
        } else if (property instanceof ColorProperty) {
            return new ColorPropertyEditor((ColorProperty)property);
        }

        return null;
    }

    public void setTarget(IUpdateable target) {
        this.target = target;
        prepareLayout();
    }
}

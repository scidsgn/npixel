package com.npixel.gui.nodeeditor.properties;

import com.npixel.base.node.properties.OptionNodeProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class OptionPropertyEditor extends HBox {
    private final OptionNodeProperty property;

    public OptionPropertyEditor(OptionNodeProperty property) {
        this.property = property;

        prepareLayout();
    }

    private void prepareLayout() {
        ComboBox<String> combo = new ComboBox<>();
        HBox.setHgrow(combo, Priority.ALWAYS);

        for (String v : property.getValues()) {
            combo.getItems().add(v);
        }
        combo.getSelectionModel().select(property.getValue());
        combo.setOnAction(event -> property.setValue(combo.getSelectionModel().getSelectedIndex()));

        this.getChildren().add(combo);
    }
}

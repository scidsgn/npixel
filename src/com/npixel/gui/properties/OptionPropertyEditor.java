package com.npixel.gui.properties;

import com.npixel.base.properties.OptionProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class OptionPropertyEditor extends HBox {
    private final OptionProperty property;

    public OptionPropertyEditor(OptionProperty property) {
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

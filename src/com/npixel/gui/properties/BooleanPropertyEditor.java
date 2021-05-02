package com.npixel.gui.properties;

import com.npixel.base.properties.BooleanProperty;
import javafx.scene.control.CheckBox;

public class BooleanPropertyEditor extends CheckBox {
    private final BooleanProperty property;

    public BooleanPropertyEditor(BooleanProperty property) {
        super(property.getName());

        this.property = property;

        prepareLayout();
    }

    private void prepareLayout() {
        setSelected(property.getValue());
        setOnAction(event -> property.setValue(isSelected()));
    }
}

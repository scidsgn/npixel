package com.npixel.gui.nodeeditor.properties;

import com.npixel.base.node.properties.DoubleNodeProperty;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class DoublePropertyEditor extends HBox {
    private final DoubleNodeProperty property;

    public DoublePropertyEditor(DoubleNodeProperty property) {
        this.property = property;

        prepareLayout();
    }

    private void prepareLayout() {
        Slider slider = new Slider(property.getMinValue(), property.getMaxValue(), property.getValue());
        slider.setShowTickMarks(true);

        HBox.setHgrow(slider, Priority.ALWAYS);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            property.setValue(newValue.doubleValue());
        });

        this.getChildren().add(slider);
    }
}

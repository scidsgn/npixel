package com.npixel.gui.properties;

import com.npixel.base.properties.DoubleProperty;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class DoublePropertyEditor extends HBox {
    private final DoubleProperty property;

    public DoublePropertyEditor(DoubleProperty property) {
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

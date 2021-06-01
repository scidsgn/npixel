package com.npixel.gui.properties;

import com.npixel.base.properties.DoubleProperty;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class DoublePropertyEditor extends HBox {
    private final DoubleProperty property;

    public DoublePropertyEditor(DoubleProperty property) {
        this.property = property;

        prepareLayout();
    }

    private void handleTextUpdate(TextField valueField, Slider slider) {
        double value = Double.parseDouble(valueField.getText());
        if (value >= property.getMinValue() && value <= property.getMaxValue()) {
            property.setValue(value);
            slider.setValue(value);
        } else {
            valueField.setText(Double.toString(property.getValue()));
        }
    }

    private void prepareLayout() {
        Slider slider = new Slider(property.getMinValue(), property.getMaxValue(), property.getValue());
        slider.setShowTickMarks(true);
        HBox.setHgrow(slider, Priority.ALWAYS);

        TextField valueField = new TextField();
        valueField.setText(Double.toString(property.getValue()));
        valueField.setMaxWidth(80);

        valueField.setOnAction(event -> handleTextUpdate(valueField, slider));
        valueField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                handleTextUpdate(valueField, slider);
            }
        });

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            property.setValue(newValue.doubleValue());
            valueField.setText(Double.toString(property.getValue()));
        });

        this.getChildren().addAll(slider, valueField);
    }
}

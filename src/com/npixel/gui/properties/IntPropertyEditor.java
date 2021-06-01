package com.npixel.gui.properties;

import com.npixel.base.properties.IntProperty;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class IntPropertyEditor extends HBox {
    private final IntProperty property;

    public IntPropertyEditor(IntProperty property) {
        this.property = property;

        prepareLayout();
    }

    private void handleTextUpdate(TextField valueField, Slider slider) {
        int value = Integer.parseInt(valueField.getText());
        if (value >= property.getMinValue() && value <= property.getMaxValue()) {
            property.setValue(value);
            slider.setValue(value);
        } else {
            valueField.setText(Integer.toString(property.getValue()));
        }
    }

    private void prepareLayout() {
        Slider slider = new Slider(property.getMinValue(), property.getMaxValue(), property.getValue());
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        HBox.setHgrow(slider, Priority.ALWAYS);

        TextField valueField = new TextField();
        valueField.setText(Integer.toString(property.getValue()));
        valueField.setMaxWidth(80);

        valueField.setOnAction(event -> handleTextUpdate(valueField, slider));
        valueField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                handleTextUpdate(valueField, slider);
            }
        });

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            property.setValue(newValue.intValue());
            valueField.setText(Integer.toString(property.getValue()));
        });

        this.getChildren().addAll(slider, valueField);
    }
}

package com.npixel.gui.properties;

import com.npixel.base.bitmap.Color;
import com.npixel.base.properties.ColorProperty;
import com.npixel.base.properties.OptionProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ColorPropertyEditor extends HBox {
    private final ColorProperty property;

    public ColorPropertyEditor(ColorProperty property) {
        this.property = property;

        prepareLayout();
    }

    private void prepareLayout() {
        ColorPicker picker = new ColorPicker(property.getValue().getFXColor());
        HBox.setHgrow(picker, Priority.ALWAYS);

        Button setFromFg = new Button("fg");
        Button setFromBg = new Button("bg");

        picker.setOnAction(event -> property.setValue(new Color(picker.getValue())));
        setFromBg.setOnAction(event -> {
            Color c = property.getTargetObject().getDocument().getBackgroundColor();
            property.setValue(c);
            picker.setValue(c.getFXColor());
        });
        setFromFg.setOnAction(event -> {
            Color c = property.getTargetObject().getDocument().getForegroundColor();
            property.setValue(c);
            picker.setValue(c.getFXColor());
        });

        this.getChildren().addAll(picker, setFromFg, setFromBg);
    }
}

package com.npixel.gui.sidepanel;

import com.npixel.base.Document;
import com.npixel.base.DocumentEvent;
import com.npixel.base.bitmap.Color;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;

public class ColorPanel extends HBox {
    private final Document doc;

    public ColorPanel(Document doc) {
        this.doc = doc;

        prepareLayout();
    }

    private void prepareLayout() {
        ColorPicker fgPicker = new ColorPicker(doc.getForegroundColor().getFXColor());
        ColorPicker bgPicker = new ColorPicker(doc.getBackgroundColor().getFXColor());

        fgPicker.setOnAction(event -> doc.setForegroundColor(new Color(fgPicker.getValue())));
        bgPicker.setOnAction(event -> doc.setBackgroundColor(new Color(bgPicker.getValue())));

        doc.on(DocumentEvent.COLORSUPDATED, doc -> {
            fgPicker.setValue(doc.getForegroundColor().getFXColor());
            bgPicker.setValue(doc.getBackgroundColor().getFXColor());
            return null;
        });

        getChildren().addAll(fgPicker, bgPicker);
    }
}

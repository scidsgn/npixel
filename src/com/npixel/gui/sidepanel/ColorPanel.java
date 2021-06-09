package com.npixel.gui.sidepanel;

import com.npixel.base.Document;
import com.npixel.base.DocumentEvent;
import com.npixel.base.bitmap.Color;
import com.npixel.base.palette.NamedColor;
import com.npixel.base.palette.Palette;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class ColorPanel extends VBox {
    private final Document doc;
    private TilePane tilePane;

    public ColorPanel(Document doc) {
        this.doc = doc;

        prepareLayout();
    }

    private void prepareLayout() {
        HBox colorBar = new HBox();

        ColorPicker fgPicker = new ColorPicker(doc.getForegroundColor().getFXColor());
        ColorPicker bgPicker = new ColorPicker(doc.getBackgroundColor().getFXColor());

        fgPicker.setOnAction(event -> doc.setForegroundColor(new Color(fgPicker.getValue())));
        bgPicker.setOnAction(event -> doc.setBackgroundColor(new Color(bgPicker.getValue())));

        doc.on(DocumentEvent.COLORSUPDATED, doc -> {
            fgPicker.setValue(doc.getForegroundColor().getFXColor());
            bgPicker.setValue(doc.getBackgroundColor().getFXColor());
            return null;
        });

        colorBar.getChildren().addAll(fgPicker, bgPicker);

        ComboBox<Palette> palettePicker = new ComboBox<>(doc.getPalettes());
        palettePicker.setValue(doc.getPalettes().get(0));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMaxHeight(200);
        scrollPane.setFitToWidth(true);

        tilePane = new TilePane();

        updatePane(palettePicker.getValue());
        palettePicker.setOnAction(event -> updatePane(palettePicker.getValue()));

        scrollPane.setContent(tilePane);

        getChildren().addAll(
                new Label("Foreground & background:"), colorBar,
                palettePicker, scrollPane
        );
    }

    private void updatePane(Palette palette) {
        tilePane.getChildren().clear();

        for (NamedColor color : palette.getColors()) {
            tilePane.getChildren().add(new ColorPanel.ColorSwatchNode(doc, color));
        }
    }

    private static class ColorSwatchNode extends Canvas {
        private final ContextMenu contextMenu;

        public ColorSwatchNode(Document doc, NamedColor color) {
            setWidth(16);
            setHeight(16);

            fillColor(color);

            contextMenu = new ContextMenu();
            prepareContextMenu(doc, color);

            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    doc.setForegroundColor(color);
                } else {
                    contextMenu.show(this, event.getScreenX(), event.getScreenY());
                }
            });
        }

        private void prepareContextMenu(Document doc, NamedColor color) {
            MenuItem colorName = new MenuItem(color.getName());
            colorName.setDisable(true);

            MenuItem setAsFg = new MenuItem("Set as foreground");
            setAsFg.setOnAction(event -> doc.setForegroundColor(color));

            MenuItem setAsBg = new MenuItem("Set as background");
            setAsBg.setOnAction(event -> doc.setBackgroundColor(color));

            contextMenu.getItems().addAll(
                    colorName,
                    setAsFg, setAsBg
            );
        }

        private void fillColor(NamedColor color) {
            GraphicsContext ctx = getGraphicsContext2D();

            ctx.setFill(color.getFXColor());
            ctx.fillRect(1, 1, 14, 14);
        }
    }
}

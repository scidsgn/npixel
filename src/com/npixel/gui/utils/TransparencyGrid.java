package com.npixel.gui.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class TransparencyGrid {
    private ImagePattern pattern;
    private WritableImage image;

    private TransparencyGrid() {
        image = new WritableImage(16, 16);
        fillGrid();

        pattern = new ImagePattern(image, 0, 0, 16, 16, false);
    }

    public ImagePattern getPattern() {
        return pattern;
    }

    private void fillGrid() {
        PixelWriter writer = image.getPixelWriter();

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                boolean odd = (x >= 8) ^ (y >= 8);

                writer.setColor(x, y, odd ? Color.WHITE : Color.LIGHTGRAY);
            }
        }
    }

    public static TransparencyGrid grid = new TransparencyGrid();
}

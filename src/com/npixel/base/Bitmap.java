package com.npixel.base;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Bitmap extends WritableImage {
    private final PixelReader reader = this.getPixelReader();
    private final PixelWriter writer = this.getPixelWriter();

    public Bitmap(int width, int height) {
        super(width, height);
    }

    public void setPixel(int x, int y, Color color) {
        writer.setColor(x, y, color);
    }

    public Color getPixel(int x, int y) {
        return reader.getColor(x, y);
    }
}

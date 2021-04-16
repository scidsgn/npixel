package com.npixel.base.bitmap;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class Bitmap extends WritableImage {
    private final PixelReader reader = this.getPixelReader();
    private final PixelWriter writer = this.getPixelWriter();

    public Bitmap(int width, int height) {
        super(width, height);
    }

    public void setPixel(int x, int y, Color color) {
        writer.setColor(x, y, color.getFXColor());
    }

    public Color getPixel(int x, int y) {
        return new Color(reader.getColor(x, y));
    }
}

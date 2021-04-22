package com.npixel.base.bitmap;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.Objects;
import java.util.function.Function;

public class Bitmap extends WritableImage {
    @FunctionalInterface
    public static interface BitmapScanFunction<A, B, C, R> {
        R apply(A a, B b, C c);

        default <V> BitmapScanFunction<A, B, C, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (A a, B b, C c) -> after.apply(apply(a, b, c));
        }
    }

    private final PixelReader reader = this.getPixelReader();
    private final PixelWriter writer = this.getPixelWriter();

    public Bitmap(int width, int height) {
        super(width, height);
    }

    public void setPixel(int x, int y, Color color) {
        if (x < getWidth() && y < getHeight()) {
            writer.setColor(x, y, color.getFXColor());
        }
    }

    public Color getPixel(int x, int y) {
        if (x >= getWidth() || y >= getHeight()) {
            return new Color(0, 0, 0, 0);
        }

        return new Color(reader.getColor(x, y));
    }

    public void scan(BitmapScanFunction<Integer, Integer, Color, Color> scanFunction) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                setPixel(x, y, scanFunction.apply(x, y, getPixel(x, y)));
            }
        }
    }

    public static Bitmap createEncompassing(Bitmap... bitmaps) {
        int w = 1, h = 1;

        for (Bitmap bitmap : bitmaps) {
            w = (int)Math.max(w, bitmap.getWidth());
            h = (int)Math.max(h, bitmap.getHeight());
        }

        return new Bitmap(w, h);
    }
}

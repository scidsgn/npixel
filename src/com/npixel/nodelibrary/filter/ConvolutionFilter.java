package com.npixel.nodelibrary.filter;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;

public class ConvolutionFilter {
    private final int size;
    private final double[][] filter;
    private double scale = 1;
    private final boolean preserveAlpha;

    public ConvolutionFilter(int size, boolean preserveAlpha) {
        this.size = size;
        this.preserveAlpha = preserveAlpha;
        this.filter = new double[size][size];
    }

    public void setData(double ...items) {
        if (items.length != size * size) {
            return;
        }

        int i = 0;
        for (double x : items) {
            int fx = i % size;
            int fy = i / size;

            filter[fx][fy] = x;

            i += 1;
        }
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    private double getValue(int dx, int dy) {
        int min = size / 2;
        return filter[dx + min][dy + min];
    }

    public Color filterPixel(Bitmap srcBitmap, int x, int y) {
        Color outColor = new Color(0, 0, 0, preserveAlpha ? 0 : 1);
        int min = size / 2;

        for (int dx = -min; dx < min + 1; dx++) {
            for (int dy = -min; dy < min + 1; dy++) {
                Color c = srcBitmap.getPixel(x + dx, y + dy);
                outColor.addScaled(c, getValue(dx, dy) * scale);
            }
        }

        return outColor;
    }
}

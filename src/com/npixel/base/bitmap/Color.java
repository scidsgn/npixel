package com.npixel.base.bitmap;

public class Color {
    private double red, green, blue, alpha;

    public Color(double r, double g, double b, double a) {
        red = r;
        green = g;
        blue = b;
        alpha = Math.max(Math.min(a, 1), 0);
    }

    public Color(double r, double g, double b) {
        red = r;
        green = g;
        blue = b;
        alpha = 1.0;
    }

    public Color() {
        red = 0.0;
        green = 0.0;
        blue = 0.0;
        alpha = 1.0;
    }

    public Color(javafx.scene.paint.Color fxColor) {
        red = fxColor.getRed();
        green = fxColor.getGreen();
        blue = fxColor.getBlue();
        alpha = fxColor.getOpacity();
    }

    public javafx.scene.paint.Color getFXColor() {
        return new javafx.scene.paint.Color(
                Math.max(Math.min(red, 1), 0),
                Math.max(Math.min(green, 1), 0),
                Math.max(Math.min(blue, 1), 0),
                Math.max(Math.min(alpha, 1), 0)
        );
    }

    public int getByteRed() {
        return (int)(255 * Math.max(Math.min(red, 1), 0));
    }

    public int getByteGreen() {
        return (int)(255 * Math.max(Math.min(green, 1), 0));
    }

    public int getByteBlue() {
        return (int)(255 * Math.max(Math.min(blue, 1), 0));
    }

    public int getByteAlpha() {
        return (int)(255 * Math.max(Math.min(alpha, 1), 0));
    }

    public double getRed() {
        return red;
    }

    public Color setRed(double v) {
        red = v;
        return this;
    }

    public double getGreen() {
        return green;
    }

    public Color setGreen(double v) {
        green = v;
        return this;
    }

    public double getBlue() {
        return blue;
    }

    public Color setBlue(double v) {
        blue = v;
        return this;
    }

    public double getAlpha() {
        return alpha;
    }

    public Color setAlpha(double v) {
        alpha = Math.max(Math.min(v, 1), 0);
        return this;
    }

    public double getLightness() {
        return 0.2126 * red + 0.7152 * green + 0.0722 * blue;
    }

    public void addScaled(Color c, double scale) {
        red += c.getRed() * scale;
        green += c.getGreen() * scale;
        blue += c.getBlue() * scale;
        alpha += c.getAlpha() * scale;
    }

    public static Color mix(Color a, Color b, double mix) {
        return new Color(
                a.getRed() + mix * (b.getRed() - a.getRed()),
                a.getGreen() + mix * (b.getGreen() - a.getGreen()),
                a.getBlue() + mix * (b.getBlue() - a.getBlue()),
                a.getAlpha() + mix * (b.getAlpha() - a.getAlpha())
        );
    }

    public static Color over(Color bg, Color fore) {
        if (bg.getAlpha() == 0) {
            return fore;
        }

        double a = fore.getAlpha() + bg.getAlpha() * (1 - fore.getAlpha());

        return new Color(
                (fore.getRed() * fore.getAlpha() + bg.getRed() * bg.getAlpha() * (1 - fore.getAlpha())) / a,
                (fore.getGreen() * fore.getAlpha() + bg.getGreen() * bg.getAlpha() * (1 - fore.getAlpha())) / a,
                (fore.getBlue() * fore.getAlpha() + bg.getBlue() * bg.getAlpha() * (1 - fore.getAlpha())) / a,
                a
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Color) {
            return ((Color) obj).getRed() == red &&
                    ((Color) obj).getGreen() == green &&
                    ((Color) obj).getBlue() == blue &&
                    ((Color) obj).getAlpha() == alpha;
        }
        return false;
    }

    public static Color TRANSPARENT = new Color(0, 0, 0, 0);
}

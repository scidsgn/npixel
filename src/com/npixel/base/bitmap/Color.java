package com.npixel.base.bitmap;

public class Color {
    private double red, green, blue, alpha;

    public Color(double r, double g, double b, double a) {
        red = r;
        green = g;
        blue = b;
        alpha = a;
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
        return new javafx.scene.paint.Color(red, green, blue, alpha);
    }

    public double getRed() {
        return red;
    }

    public void setRed(double v) {
        red = v;
    }

    public double getGreen() {
        return green;
    }

    public void setGreen(double v) {
        green = v;
    }

    public double getBlue() {
        return blue;
    }

    public void setBlue(double v) {
        blue = v;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double v) {
        alpha = v;
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
        double a = fore.getAlpha() + bg.getAlpha() * (1 - fore.getAlpha());

        return new Color(
                (fore.getRed() * fore.getAlpha() + bg.getRed() * bg.getAlpha() * (1 - fore.getAlpha())) / a,
                (fore.getGreen() * fore.getAlpha() + bg.getGreen() * bg.getAlpha() * (1 - fore.getAlpha())) / a,
                (fore.getBlue() * fore.getAlpha() + bg.getBlue() * bg.getAlpha() * (1 - fore.getAlpha())) / a,
                a
        );
    }
}

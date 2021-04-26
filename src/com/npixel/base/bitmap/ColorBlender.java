package com.npixel.base.bitmap;

import java.util.function.BiFunction;

public class ColorBlender {
    public double multiply(double a, double b) {
        return a * b;
    }

    public double colorBurn(double a, double b) {
        return 1 - (1 - a) / b;
    }

    public double linearBurn(double a, double b) {
        return a + b - 1;
    }

    public double screen(double a, double b) {
        return 1 - (1 - a) * (1 - b);
    }

    public BiFunction<Double, Double, Double> getBlendingFunction(ColorBlendMode mode) {
        return (a, b) -> b;
    }
}

package com.npixel.base.palette;

import com.npixel.base.bitmap.Color;

public class NamedColor extends Color {
    private final String name;

    public NamedColor(String name, double r, double g, double b) {
        super(r, g, b);

        this.name = name;
    }

    public String getName() {
        return name;
    }
}

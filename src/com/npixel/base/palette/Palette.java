package com.npixel.base.palette;

import java.util.ArrayList;
import java.util.List;

public class Palette {
    protected final String name;
    protected final List<NamedColor> colors;

    public Palette(String name) {
        this.name = name;
        this.colors = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<NamedColor> getColors() {
        return colors;
    }

    @Override
    public String toString() {
        return name;
    }
}

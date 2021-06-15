package com.npixel.nodelibrary.distort;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.DoubleProperty;
import com.npixel.base.properties.OptionProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class DistortMapNode extends Node {
    public DistortMapNode(NodeTree tree) {
        super(tree);

        typeString = "DistortMap";
        icon = Icons.getIcon("distortmap");

        propertyGroups.add(new PropertyGroup(
                "x", "X axis distortion",
                new OptionProperty(this, "channel", "Source channel", 0, "Red", "Green", "Blue"),
                new DoubleProperty(this, "strength", "Strength", 1, 0, 50),
                new DoubleProperty(this, "shift", "Shift", 0, 0, 1)
        ));
        propertyGroups.add(new PropertyGroup(
                "y", "Y axis distortion",
                new OptionProperty(this, "channel", "Source channel", 1, "Red", "Green", "Blue"),
                new DoubleProperty(this, "strength", "Strength", 1, 0, 50),
                new DoubleProperty(this, "shift", "Shift", 0, 0, 1)
        ));
        propertyGroups.add(new PropertyGroup(
                "bounds", "Image bounds extension",
                new OptionProperty(this, "method", "Method", 0, "Transparent", "Extend", "Wrap")
        ));

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Input", new Bitmap(1, 1)));
        inputs.add(new NodeSocket(this, "map", NodeSocketType.INPUT, "Map", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(1, 1), "in"));
    }

    private double getChannel(Color c, int channel) {
        if (channel == 0) {
            return c.getRed();
        } else if (channel == 1) {
            return c.getGreen();
        }

        return c.getBlue();
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");
        Bitmap mapBitmap = (Bitmap)getInputValue("map");

        Bitmap outBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("out").setValue(outBitmap);

        int extMethod = ((OptionProperty)getProperty("bounds", "method")).getValue();
        
        int xChannel = ((OptionProperty)getProperty("x", "channel")).getValue();
        int yChannel = ((OptionProperty)getProperty("y", "channel")).getValue();

        double xStrength = ((DoubleProperty)getProperty("x", "strength")).getValue();
        double xShift = ((DoubleProperty)getProperty("x", "shift")).getValue();
        double yStrength = ((DoubleProperty)getProperty("y", "strength")).getValue();
        double yShift = ((DoubleProperty)getProperty("y", "shift")).getValue();

        outBitmap.scan((x, y, color) -> {
            Color mapColor = mapBitmap.getPixel(x, y);
            double xDistort = xStrength * getChannel(mapColor, xChannel) - xStrength * xShift;
            double yDistort = yStrength * getChannel(mapColor, yChannel) - yStrength * yShift;
            int dx = x + (int)xDistort;
            int dy = y + (int)yDistort;

            if (extMethod == 1) {
                dx = Math.max(Math.min(dx, (int)inBitmap.getWidth() - 1), 0);
                dy = Math.max(Math.min(dy, (int)inBitmap.getHeight() - 1), 0);
            } else if (extMethod == 2) {
                if (dx < 0) {
                    dx += inBitmap.getWidth();
                } else {
                    dx = dx % (int)inBitmap.getWidth();
                }
                if (dy < 0) {
                    dy += inBitmap.getHeight();
                } else {
                    dy = dy % (int)inBitmap.getHeight();
                }
            }

            return inBitmap.getPixel(dx, dy);
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

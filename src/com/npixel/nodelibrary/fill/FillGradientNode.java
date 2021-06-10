package com.npixel.nodelibrary.fill;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.*;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class FillGradientNode extends Node {
    public FillGradientNode(NodeTree tree) {
        super(tree);

        typeString = "FillGradient";
        icon = Icons.getIcon("none");


        propertyGroups.add(new PropertyGroup(
                "gradient", "Gradient",
                new OptionProperty(
                        this, "type", "Gradient type", 0,
                        "Horizontal linear", "Vertical linear",
                        "Radial", "Angle"
                ),
                new DoubleProperty(this, "shift", "Shift", 0, 0, 1),
                new DoubleProperty(this, "repeat", "Repeat", 1, 1, 6),
                new BooleanProperty(this, "reverse", "Reverse", false),
                new BooleanProperty(this, "pingpong", "Ping-pong", false)
        ));

        propertyGroups.add(new PropertyGroup(
                "fill", "Fill",
                new ColorProperty(this, "color1", "Color 1", new Color()),
                new ColorProperty(this, "color2", "Color 2", new Color(1, 1, 1)),
                new DoubleProperty(this, "mix", "Mix", 1, 0, 1)
        ));

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Input", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Shape", new Bitmap(1, 1)));
    }

    private double calculateGradient(double x, double y, int type) {
        if (type == 1) {
            return y;
        } else if (type == 2) {
            return Math.hypot(x - 0.5, y - 0.5) / (Math.sqrt(2) / 2);
        } else if (type == 3) {
            return (Math.atan2(x - 0.5, y - 0.5) + Math.PI) / (2 * Math.PI);
        }
        return x;
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");

        Bitmap outBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("out").setValue(outBitmap);

        int type = ((OptionProperty)getProperty("gradient", "type")).getValue();
        double shift = ((DoubleProperty)getProperty("gradient", "shift")).getValue();
        double repeat = ((DoubleProperty)getProperty("gradient", "repeat")).getValue();
        boolean reverse = ((BooleanProperty)getProperty("gradient", "reverse")).getValue();
        boolean pingpong = ((BooleanProperty)getProperty("gradient", "pingpong")).getValue();

        Color color1 = ((ColorProperty)getProperty("fill", "color1")).getValue();
        Color color2 = ((ColorProperty)getProperty("fill", "color2")).getValue();
        double mix = ((DoubleProperty)getProperty("fill", "mix")).getValue();

        outBitmap.scan((x, y, c) -> {
            Color in = inBitmap.getPixel(x, y);

            double gradient = calculateGradient((double) x / outBitmap.getWidth(), (double) y / outBitmap.getHeight(), type);
            gradient = gradient * repeat + shift;

            if (pingpong) {
                gradient = 0.5 * gradient - Math.floor(0.5 * gradient);
                gradient = 1 - Math.abs(gradient * 2 - 1);
            } else {
                gradient = gradient - Math.floor(gradient);
            }
            if (reverse) {
                gradient = 1 - gradient;
            }

            Color grad = Color.mix(color1, color2, gradient);
            grad = grad.setAlpha(in.getAlpha() * grad.getAlpha());

            return Color.mix(in, grad, mix);
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

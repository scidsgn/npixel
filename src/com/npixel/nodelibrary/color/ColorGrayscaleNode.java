package com.npixel.nodelibrary.color;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.DoubleProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class ColorGrayscaleNode extends Node {
    public ColorGrayscaleNode(NodeTree tree) {
        super(tree);

        typeString = "ColorGrayscale";
        icon = Icons.getIcon("none");

        propertyGroups.add(new PropertyGroup(
                "bw", "Grayscale",
                new DoubleProperty(this, "red", "Red influence", 0.2126, 0, 1),
                new DoubleProperty(this, "green", "Green influence", 0.7152, 0, 1),
                new DoubleProperty(this, "blue", "Blue influence", 0.0722, 0, 1),
                new DoubleProperty(this, "mix", "Mix", 1, 0, 1)
        ));

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Input", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(1, 1)));
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");

        Bitmap outBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("out").setValue(outBitmap);

        double red = ((DoubleProperty)getProperty("bw", "red")).getValue();
        double green = ((DoubleProperty)getProperty("bw", "green")).getValue();
        double blue = ((DoubleProperty)getProperty("bw", "blue")).getValue();
        double mix = ((DoubleProperty)getProperty("bw", "mix")).getValue();

        outBitmap.scan((x, y, c) -> {
            Color in = inBitmap.getPixel(x, y);
            double bw = red * in.getRed() + green * in.getGreen() + blue * in.getBlue();

            return Color.mix(in, new Color(bw, bw, bw, in.getAlpha()), mix);
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

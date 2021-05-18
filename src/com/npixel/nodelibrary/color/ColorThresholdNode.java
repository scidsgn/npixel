package com.npixel.nodelibrary.color;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.*;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class ColorThresholdNode extends Node {
    public ColorThresholdNode(NodeTree tree) {
        super(tree);

        typeString = "ColorThreshold";
        icon = Icons.getIcon("none");

        propertyGroups.add(new PropertyGroup(
                "threshold", "Threshold",
                new OptionProperty(this, "channel", "Reference", 0, "Lightness", "Red", "Green", "Blue"),
                new DoubleProperty(this, "cutoff", "Cutoff", 0.5, 0, 1),
                new ColorProperty(this, "low", "Under cutoff", new Color(0, 0, 0)),
                new ColorProperty(this, "high", "Over cutoff", new Color(1, 1, 1)),
                new DoubleProperty(this, "mix", "Mix", 1, 0, 1)
        ));

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Input", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(1, 1)));
    }

    private double getChannel(Color c, int channel) {
        if (channel == 1) {
            return c.getRed();
        } else if (channel == 2) {
            return c.getGreen();
        } else if (channel == 3) {
            return c.getBlue();
        }

        return c.getLightness();
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");

        Bitmap outBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("out").setValue(outBitmap);

        int channel = ((OptionProperty)getProperty("threshold", "channel")).getValue();
        double cutoff = ((DoubleProperty)getProperty("threshold", "cutoff")).getValue();
        Color lowColor = ((ColorProperty)getProperty("threshold", "low")).getValue();
        Color highColor = ((ColorProperty)getProperty("threshold", "high")).getValue();
        double mix = ((DoubleProperty)getProperty("threshold", "mix")).getValue();

        outBitmap.scan((x, y, c) -> {
            Color in = inBitmap.getPixel(x, y);
            return Color.mix(in, (getChannel(in, channel) >= cutoff) ? highColor : lowColor, mix).setAlpha(in.getAlpha());
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

package com.npixel.nodelibrary.color;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.ColorProperty;
import com.npixel.base.properties.DoubleProperty;
import com.npixel.base.properties.OptionProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class ColorDuotoneNode extends Node {
    public ColorDuotoneNode(NodeTree tree) {
        super(tree);

        typeString = "ColorDuotone";
        icon = Icons.getIcon("colorduotone");

        propertyGroups.add(new PropertyGroup(
                "duotone", "Duotone",
                new OptionProperty(this, "channel", "Reference", 0, "Lightness", "Red", "Green", "Blue"),
                new ColorProperty(this, "low", "Shadows", new Color(0, 0, 0)),
                new ColorProperty(this, "high", "Highlights", new Color(1, 1, 1)),
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

        int channel = ((OptionProperty)getProperty("duotone", "channel")).getValue();
        Color lowColor = ((ColorProperty)getProperty("duotone", "low")).getValue();
        Color highColor = ((ColorProperty)getProperty("duotone", "high")).getValue();
        double mix = ((DoubleProperty)getProperty("duotone", "mix")).getValue();

        outBitmap.scan((x, y, c) -> {
            Color in = inBitmap.getPixel(x, y);
            return Color.mix(in, Color.mix(lowColor, highColor, getChannel(in, channel)).setAlpha(in.getAlpha()), mix);
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

package com.npixel.nodelibrary.composite;

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

public class CompositeMaskNode extends Node {
    public CompositeMaskNode(NodeTree tree) {
        super(tree);

        typeString = "CompMask";
        icon = Icons.getIcon("compmask");

        propertyGroups.add(new PropertyGroup(
                "mask", "Masking",
                new OptionProperty(
                        this, "mode", "Mode", 0,
                        "Lightness to Alpha", "Red to Alpha", "Green to Alpha", "Blue to Alpha",
                        "Alpha to Alpha"
                ),
                new DoubleProperty(this, "strength", "Strength", 1, 0, 1)
        ));

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Input", new Bitmap(1, 1)));
        inputs.add(new NodeSocket(this, "mask", NodeSocketType.INPUT, "Mask", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(1, 1)));
    }

    private double getMaskValue(Color maskColor, int mode) {
        if (mode == 0) { // Lightness to Alpha
            return maskColor.getLightness();
        } else if (mode == 1) { // Red to Alpha
            return maskColor.getRed();
        } else if (mode == 2) { // Green to Alpha
            return maskColor.getGreen();
        } else if (mode == 3) { // Blue to Alpha
            return maskColor.getBlue();
        } else if (mode == 4) { // Alpha to Alpha
            return maskColor.getAlpha();
        }

        return 0.0;
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");
        Bitmap maskBitmap = (Bitmap)getInputValue("mask");

        Bitmap outBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("out").setValue(outBitmap);

        int maskMode = ((OptionProperty)getProperty("mask", "mode")).getValue();
        double maskStrength = ((DoubleProperty)getProperty("mask", "strength")).getValue();

        outBitmap.scan((x, y, color) -> {
            Color bmpColor = inBitmap.getPixel(x, y);
            Color maskColor = maskBitmap.getPixel(x, y);

            double bmpAlpha = bmpColor.getAlpha();
            double maskedAlpha = bmpAlpha * getMaskValue(maskColor, maskMode);

            return bmpColor.setAlpha(bmpAlpha + maskStrength * (maskedAlpha - bmpAlpha));
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

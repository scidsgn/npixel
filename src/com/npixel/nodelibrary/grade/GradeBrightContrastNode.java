package com.npixel.nodelibrary.grade;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.DoubleProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

import static java.awt.Color.black;

public class GradeBrightContrastNode extends Node {
    public GradeBrightContrastNode(NodeTree tree) {
        super(tree);

        typeString = "GradeBrightContrast";
        icon = Icons.getIcon("none");

        propertyGroups.add(new PropertyGroup(
                "bc", "Brightness & Contrast",
                new DoubleProperty(this, "bright", "Brightness", 0, -1, 1),
                new DoubleProperty(this, "contrast", "Contrast", 0, -1, 1),
                new DoubleProperty(this, "mix", "Mix", 1, 0, 1)
        ));

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Input", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(1, 1)));
    }

    private double calculateBC(double channel, double bright, double contrast) {
        double slope = Math.pow(2.0, contrast);
        return (channel - 0.5) * slope + 0.5 + bright;
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");

        Bitmap outBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("out").setValue(outBitmap);

        double bright = ((DoubleProperty)getProperty("bc", "bright")).getValue();
        double contrast = ((DoubleProperty)getProperty("bc", "contrast")).getValue();
        double mix = ((DoubleProperty)getProperty("bc", "mix")).getValue();

        outBitmap.scan((x, y, c) -> {
            Color in = inBitmap.getPixel(x, y);
            Color lvl = new Color(
                    calculateBC(in.getRed(), bright, contrast),
                    calculateBC(in.getGreen(), bright, contrast),
                    calculateBC(in.getBlue(), bright, contrast),
                    in.getAlpha()
            );

            return Color.mix(in, lvl, mix);
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

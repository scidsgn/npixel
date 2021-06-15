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

public class GradeLevelsNode extends Node {
    public GradeLevelsNode(NodeTree tree) {
        super(tree);

        typeString = "GradeLevels";
        icon = Icons.getIcon("gradelevels");

        propertyGroups.add(new PropertyGroup(
                "levels", "Levels",
                new DoubleProperty(this, "black", "Black point", 0, 0, 1),
                new DoubleProperty(this, "gray", "Neutral gray point", 0.5, 0, 1),
                new DoubleProperty(this, "white", "White point", 1, 0, 1),
                new DoubleProperty(this, "mix", "Mix", 1, 0, 1)
        ));

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Input", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(1, 1)));
    }

    private double calculateLevels(double channel, double black, double gray, double white) {
        if (channel < 0.5) {
            return black + (channel * 2) * (gray - black);
        } else {
            return gray + (channel * 2 - 1) * (white - gray);
        }
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");

        Bitmap outBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("out").setValue(outBitmap);

        double black = ((DoubleProperty)getProperty("levels", "black")).getValue();
        double gray = ((DoubleProperty)getProperty("levels", "gray")).getValue();
        double white = ((DoubleProperty)getProperty("levels", "white")).getValue();
        double mix = ((DoubleProperty)getProperty("levels", "mix")).getValue();

        outBitmap.scan((x, y, c) -> {
            Color in = inBitmap.getPixel(x, y);
            Color lvl = new Color(
                    calculateLevels(in.getRed(), black, gray, white),
                    calculateLevels(in.getGreen(), black, gray, white),
                    calculateLevels(in.getBlue(), black, gray, white),
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

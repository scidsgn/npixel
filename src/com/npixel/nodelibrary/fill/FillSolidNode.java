package com.npixel.nodelibrary.fill;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.*;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class FillSolidNode extends Node {
    public FillSolidNode(NodeTree tree) {
        super(tree);

        typeString = "FillSolid";
        icon = Icons.getIcon("none");
        propertyGroups.add(new PropertyGroup(
                "fill", "Fill",
                new ColorProperty(this, "color", "Color", new Color()),
                new DoubleProperty(this, "mix", "Mix", 1, 0, 1)
        ));

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Input", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Shape", new Bitmap(1, 1)));
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");

        Bitmap outBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("out").setValue(outBitmap);

        Color color = ((ColorProperty)getProperty("fill", "color")).getValue();
        double mix = ((DoubleProperty)getProperty("fill", "mix")).getValue();

        outBitmap.scan((x, y, c) -> {
            Color in = inBitmap.getPixel(x, y);
            return Color.mix(
                    in,
                    new Color(color.getRed(), color.getGreen(), color.getBlue(), in.getAlpha() * color.getAlpha()),
                    mix
            );
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

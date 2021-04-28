package com.npixel.nodelibrary.color;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.DoubleProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.tree.NodeTree;

public class ColorCrossfadeNode extends Node {
    public ColorCrossfadeNode(NodeTree tree) {
        super(tree);

        typeString = "ColorXfade";

        propertyGroups.add(new PropertyGroup(
                "xfade", "Crossfade",
                new DoubleProperty(this, "value", "A/B fade", 0.5, 0, 1)
        ));

        inputs.add(new NodeSocket(this, "a", NodeSocketType.INPUT, "A", new Bitmap(1, 1)));
        inputs.add(new NodeSocket(this, "b", NodeSocketType.INPUT, "B", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(1, 1)));
    }

    @Override
    public void process() {
        Bitmap outBitmap = Bitmap.createEncompassing((Bitmap)getInputValue("a"), (Bitmap)getInputValue("b"));
        getOutput("out").setValue(outBitmap);

        Bitmap bmpA = (Bitmap)getInputValue("a");
        Bitmap bmpB = (Bitmap)getInputValue("b");

        double xfadeValue = ((DoubleProperty)getProperty("xfade", "value")).getValue();

        outBitmap.scan((x, y, color) -> Color.mix(bmpA.getPixel(x, y), bmpB.getPixel(x, y), xfadeValue));

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

package com.npixel.nodelibrary.color;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class ColorInvertNode extends Node {
    public ColorInvertNode(NodeTree tree) {
        super(tree);

        typeString = "ColorInvert";
        icon = Icons.getIcon("none");

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Input", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(1, 1)));
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");

        Bitmap outBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("out").setValue(outBitmap);

        outBitmap.scan((x, y, c) -> {
            Color inColor = inBitmap.getPixel(x, y);
            return new Color(1 - inColor.getRed(), 1 - inColor.getGreen(), 1 - inColor.getBlue(), inColor.getAlpha());
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

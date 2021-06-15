package com.npixel.nodelibrary.transform;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.BooleanProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class TransformFlipNode extends Node {
    public TransformFlipNode(NodeTree tree) {
        super(tree);

        typeString = "TransformFlip";
        icon = Icons.getIcon("transformflip");

        propertyGroups.add(new PropertyGroup("flip", "Flip",
                new BooleanProperty(this, "x", "Flip X", false),
                new BooleanProperty(this, "y", "Flip Y", false)
        ));

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Bitmap", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Bitmap", new Bitmap(1, 1)));
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");

        Bitmap outBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("out").setValue(outBitmap);

        boolean flipX = ((BooleanProperty)getProperty("flip", "x")).getValue();
        boolean flipY = ((BooleanProperty)getProperty("flip", "y")).getValue();

        outBitmap.scan((x, y, c) -> {
            if (flipX) {
                x = (int)inBitmap.getWidth() - x - 1;
            }
            if (flipY) {
                y = (int)inBitmap.getHeight() - y - 1;
            }

            return inBitmap.getPixel(x, y);
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

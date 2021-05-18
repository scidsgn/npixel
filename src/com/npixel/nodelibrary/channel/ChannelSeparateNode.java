package com.npixel.nodelibrary.channel;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.DoubleProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class ChannelSeparateNode extends Node {
    public ChannelSeparateNode(NodeTree tree) {
        super(tree);

        typeString = "ChannelSeparate";
        icon = Icons.getIcon("none");

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Input", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "red", NodeSocketType.OUTPUT, "Red", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "green", NodeSocketType.OUTPUT, "Green", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "blue", NodeSocketType.OUTPUT, "Blue", new Bitmap(1, 1)));
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");

        Bitmap redBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("red").setValue(redBitmap);
        Bitmap greenBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("green").setValue(greenBitmap);
        Bitmap blueBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("blue").setValue(blueBitmap);

        inBitmap.scanNoSet((x, y, c) -> {
            redBitmap.setPixel(x, y, new Color(c.getRed(), 0, 0, c.getAlpha()));
            greenBitmap.setPixel(x, y, new Color(0, c.getGreen(), 0, c.getAlpha()));
            blueBitmap.setPixel(x, y, new Color(0, 0, c.getBlue(), c.getAlpha()));

            return null;
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("red").getValue();
    }
}

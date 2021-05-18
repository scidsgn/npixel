package com.npixel.nodelibrary.p3d;

import com.npixel.base.Vector;
import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.DoubleProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class P3DNormalMapNode extends Node {
    public P3DNormalMapNode(NodeTree tree) {
        super(tree);

        typeString = "P3DNormalMap";
        icon = Icons.getIcon("none");

        propertyGroups.add(new PropertyGroup(
                "nmap", "Normal Map",
                new DoubleProperty(this, "height", "Height", 1, 0.1, 5)
        ));

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Height map", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Normal map", new Bitmap(1, 1), "in"));
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");

        Bitmap outBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("out").setValue(outBitmap);

        double height = ((DoubleProperty)getProperty("nmap", "height")).getValue();

        outBitmap.scan((x, y, color) -> {
            double xDiff = inBitmap.getPixel(x + 1, y).getLightness() - inBitmap.getPixel(x - 1, y).getLightness();
            double yDiff = inBitmap.getPixel(x, y + 1).getLightness() - inBitmap.getPixel(x, y - 1).getLightness();
            Vector normal = new Vector(xDiff, yDiff, 1 / height).normalize().scale(0.5).add(new Vector(0.5, 0.5, 0.5));

            return new Color(normal.getX(), normal.getY(), normal.getZ());
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

package com.npixel.nodelibrary.shape;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.*;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class ShapeRectangleNode extends Node {
    public ShapeRectangleNode(NodeTree tree) {
        super(tree);

        typeString = "ShapeRectangle";
        icon = Icons.getIcon("shaperectangle");

        propertyGroups.add(new PropertyGroup(
                "size", "Size",
                new BooleanProperty(this, "uniform", "1:1 ratio (use only Width)", false),
                new IntProperty(this, "width", "Width", 32, 1, 200),
                new IntProperty(this, "height", "Height", 32, 1, 200)
        ));
        propertyGroups.add(new PropertyGroup(
                "fill", "Fill",
                new ColorProperty(this, "color", "Color", new Color())
        ));

        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Shape", new Bitmap(1, 1)));
    }

    @Override
    public void process() {
        int width = ((IntProperty)getProperty("size", "width")).getValue();
        int height = ((IntProperty)getProperty("size", "height")).getValue();
        boolean sizeUniform = ((BooleanProperty)getProperty("size", "uniform")).getValue();

        int w = width;
        int h;
        if (sizeUniform) {
            h = width;
        } else {
            h = height;
        }

        Color color = ((ColorProperty)getProperty("fill", "color")).getValue();

        Bitmap outBitmap = new Bitmap(w, h);
        getOutput("out").setValue(outBitmap);

        outBitmap.scan((x, y, c) -> color);

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

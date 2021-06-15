package com.npixel.nodelibrary.shape;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.*;
import com.npixel.base.properties.templates.SizePropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class ShapeEllipseNode extends Node {
    public ShapeEllipseNode(NodeTree tree) {
        super(tree);

        typeString = "ShapeEllipse";
        icon = Icons.getIcon("shapeellipse");

        propertyGroups.add(new SizePropertyGroup(this, "size", "Size", 32, 32, 1, 512));
        propertyGroups.add(new PropertyGroup(
                "shape", "Shape",
                new BooleanProperty(this, "uniform", "Uniform shape (use only Horizontal exponent)", false),
                new DoubleProperty(this, "exph", "Horizontal exponent", 2, 0.5, 5),
                new DoubleProperty(this, "expv", "Vertical exponent", 2, 0.5, 5)
        ));
        propertyGroups.add(new PropertyGroup(
                "fill", "Fill",
                new ColorProperty(this, "color", "Color", new Color())
        ));

        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Shape", new Bitmap(1, 1)));
    }

    @Override
    public void process() {
        int w = SizePropertyGroup.getWidth(this, "size");
        int h = SizePropertyGroup.getHeight(this, "size");

        double exph = ((DoubleProperty)getProperty("shape", "exph")).getValue();
        double expv = ((DoubleProperty)getProperty("shape", "expv")).getValue();
        boolean shapeUniform = ((BooleanProperty)getProperty("shape", "uniform")).getValue();

        Color color = ((ColorProperty)getProperty("fill", "color")).getValue();

        Bitmap outBitmap = new Bitmap(w, h);
        getOutput("out").setValue(outBitmap);

        outBitmap.scan((x, y, c) -> {
            double fx = (double)x * 2 - w;
            double fy = (double)y * 2 - h;

            boolean inside = Math.pow(Math.abs(fx / w), exph) + Math.pow(Math.abs(fy / h), shapeUniform ? exph : expv) <= 1;

            return inside ? color : Color.TRANSPARENT;
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

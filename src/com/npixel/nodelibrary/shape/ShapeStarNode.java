package com.npixel.nodelibrary.shape;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.ColorProperty;
import com.npixel.base.properties.DoubleProperty;
import com.npixel.base.properties.IntProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.properties.templates.SizePropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class ShapeStarNode extends Node {
    public ShapeStarNode(NodeTree tree) {
        super(tree);

        typeString = "ShapeStar";
        icon = Icons.getIcon("none");

        propertyGroups.add(new SizePropertyGroup(this, "size", "Size", 32, 32, 1, 500));
        propertyGroups.add(new PropertyGroup(
                "shape", "Shape",
                new IntProperty(this, "sides", "Sides", 3, 3, 20),
                new IntProperty(this, "rotation", "Rotation", 0, -180, 180),
                new DoubleProperty(this, "starRadius", "Inner radius", 0.6, 0, 1)
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

        Color color = ((ColorProperty)getProperty("fill", "color")).getValue();

        int n = ((IntProperty)getProperty("shape", "sides")).getValue() * 2;
        double starRadius = ((DoubleProperty)getProperty("shape", "starRadius")).getValue();

        int rotDeg = ((IntProperty)getProperty("shape", "rotation")).getValue();
        double rot = (double)rotDeg * Math.PI / 180;

        Bitmap outBitmap = new Bitmap(w, h);
        getOutput("out").setValue(outBitmap);

        double[] xs = new double[n];
        double[] ys = new double[n];
        for (int i = 0; i < n; i++) {
            double theta = (double) i / n * 2 * Math.PI + rot;
            double radius = (i % 2 == 0) ? 1 : starRadius;
            xs[i] = (0.5 + radius * Math.cos(theta) / 2) * w;
            ys[i] = (0.5 + radius * Math.sin(theta) / 2) * h;
        }

        outBitmap.scan((x, y, c) -> {
            for (int i = 0; i < n; i++) {
                double refX = xs[(i + 1) % n] - xs[i];
                double refY = ys[(i + 1) % n] - ys[i];
                double relX = x - xs[i];
                double relY = y - ys[i];

                if (refX * relY - refY * relX < 0) {
                    return Color.TRANSPARENT;
                }
            }

            return color;
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

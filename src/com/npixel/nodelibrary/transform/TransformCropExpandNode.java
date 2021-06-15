package com.npixel.nodelibrary.transform;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.OptionProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.properties.templates.SizePropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class TransformCropExpandNode extends Node {
    public TransformCropExpandNode(NodeTree tree) {
        super(tree);

        typeString = "TransformCropExpand";
        icon = Icons.getIcon("transformcropexpand");

        propertyGroups.add(new SizePropertyGroup(this, "size", "Size", 100, 100, 1, 512));
        propertyGroups.add(new PropertyGroup("align", "Alignment",
                new OptionProperty(
                        this, "mode", "Align to", 0,
                        "Top left", "Top center", "Top right",
                        "Middle left", "Center", "Middle right",
                        "Bottom left", "Bottom center", "Bottom right"
                )
        ));

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Bitmap", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Bitmap", new Bitmap(1, 1)));
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");

        int inW = (int)inBitmap.getWidth();
        int inH = (int)inBitmap.getHeight();
        int w = SizePropertyGroup.getWidth(this, "size");
        int h = SizePropertyGroup.getHeight(this, "size");

        Bitmap outBitmap = new Bitmap(w, h);
        getOutput("out").setValue(outBitmap);

        int dx = 0, dy = 0;
        int align = ((OptionProperty)getProperty("align", "mode")).getValue();
        int alignH = align % 3;
        int alignV = align / 3;

        if (alignH == 1) {
            dx = (w - inW) / 2;
        } else if (alignH == 2) {
            dx = w - inW;
        }
        if (alignV == 1) {
            dy = (h - inH) / 2;
        } else if (alignV == 2) {
            dy = h - inH;
        }

        int finalDx = dx;
        int finalDy = dy;
        outBitmap.scan((x, y, c) -> inBitmap.getPixel(x - finalDx, y - finalDy));

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

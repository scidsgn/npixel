package com.npixel.nodelibrary.filter;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class FilterSoftenNode extends Node {
    private final ConvolutionFilter filter;

    public FilterSoftenNode(NodeTree nodeTree) {
        super(nodeTree);

        typeString = "FilterSoften";
        icon = Icons.getIcon("filtersoften");

        filter = new ConvolutionFilter(3, true);
        filter.setData(1, 1, 1, 1, 1, 1, 1, 1, 1);
        filter.setScale(1.0 / 9);

        inputs.add(new NodeSocket(this, "in", NodeSocketType.INPUT, "Input", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(1, 1), "in"));
    }

    @Override
    public void process() {
        Bitmap inBitmap = (Bitmap)getInputValue("in");

        Bitmap outBitmap = Bitmap.createEncompassing(inBitmap);
        getOutput("out").setValue(outBitmap);

        outBitmap.scan((x, y, c) -> {
            Color color = filter.filterPixel(inBitmap, x, y);

            color.setRed(Math.abs(color.getRed()));
            color.setGreen(Math.abs(color.getGreen()));
            color.setBlue(Math.abs(color.getBlue()));

            return color;
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

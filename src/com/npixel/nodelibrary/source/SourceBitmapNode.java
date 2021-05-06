package com.npixel.nodelibrary.source;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.templates.SizePropertyGroup;
import com.npixel.base.tool.ITool;
import com.npixel.base.tool.bitmap.BitmapBaseTool;
import com.npixel.base.tool.bitmap.EraserBrushTool;
import com.npixel.base.tool.bitmap.SolidBrushTool;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class SourceBitmapNode extends Node {
    public SourceBitmapNode(NodeTree tree) {
        super(tree);

        typeString = "SourceBitmap";
        icon = Icons.getIcon("sourcebitmap");

        propertyGroups.add(new SizePropertyGroup(this, "size", "Size", 100, 100, 1, 500));

        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Layer", new Bitmap(100, 100)));

        tools.add(new SolidBrushTool(this, null));
        tools.add(new EraserBrushTool(this, null));
        updateToolBitmaps();
    }

    private void updateToolBitmaps() {
        Bitmap outBitmap = (Bitmap)getOutput("out").getValue();

        for (ITool tool : tools) {
            if (tool instanceof BitmapBaseTool) {
                ((BitmapBaseTool) tool).setBitmap(outBitmap);
            }
        }
    }

    @Override
    public void process() {
        int w = SizePropertyGroup.getWidth(this, "size");
        int h = SizePropertyGroup.getHeight(this, "size");

        Bitmap outBitmap = (Bitmap)getOutput("out").getValue();

        if (w != outBitmap.getWidth() || h != outBitmap.getHeight()) {
            getOutput("out").setValue(outBitmap.cropPad(w, h));
            updateToolBitmaps();
        }

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

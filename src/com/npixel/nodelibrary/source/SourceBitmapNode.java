package com.npixel.nodelibrary.source;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.IntProperty;
import com.npixel.base.properties.templates.SizePropertyGroup;
import com.npixel.base.tool.ITool;
import com.npixel.base.tool.bitmap.BitmapBaseTool;
import com.npixel.base.tool.bitmap.EraserBrushTool;
import com.npixel.base.tool.bitmap.FloodFillTool;
import com.npixel.base.tool.bitmap.SolidBrushTool;
import com.npixel.base.tree.NodeConnection;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class SourceBitmapNode extends Node {
    public SourceBitmapNode(NodeTree tree) {
        super(tree);

        typeString = "SourceBitmap";
        icon = Icons.getIcon("sourcebitmap");

        propertyGroups.add(new SizePropertyGroup(this, "size", "Size", 100, 100, 1, 512));

        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, getName(), new Bitmap(100, 100)));

        tools.add(new SolidBrushTool(this, null));
        tools.add(new EraserBrushTool(this, null));
        tools.add(new FloodFillTool(this, null));
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

    @Override
    public void setName(String name) {
        super.setName(name);
        outputs.get(0).setName(name);
    }

    public static SourceBitmapNode createFromNode(Node node) {
        NodeTree tree = node.getTree();

        SourceBitmapNode bitmapNode = new SourceBitmapNode(tree);
        bitmapNode.setName(node.getName());
        bitmapNode.setX(node.getX());
        bitmapNode.setY(node.getY());

        tree.addNode(bitmapNode);

        if (node.getOutputs().size() > 0) {
            NodeSocket output = node.getOutputs().get(0);
            Bitmap bitmap = (Bitmap)output.getValue();

            NodeSocket bitmapOutput = bitmapNode.getOutputs().get(0);
            bitmapOutput.setValue(bitmap);

            ((IntProperty)bitmapNode.getProperty("size", "width")).setValue((int)bitmap.getWidth());
            ((IntProperty)bitmapNode.getProperty("size", "height")).setValue((int)bitmap.getHeight());

            for (NodeConnection connection : tree.findConnectionsFrom(output)) {
                tree.connect(bitmapOutput, connection.getInputSocket());
            }
        }

        tree.deleteNode(node);

        return bitmapNode;
    }
}

package com.npixel.base.tool.bitmap;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.gui.icons.Icons;
import javafx.scene.image.Image;

public class EraserBrushTool extends BrushBaseTool {
    public EraserBrushTool(Node target, Bitmap bitmap) {
        super(target, bitmap);
    }

    @Override
    public String getName() {
        return "Eraser";
    }

    @Override
    public Image getIcon() {
        return Icons.getIcon("eraser");
    }

    @Override
    protected Color getColor(int x, int y) {
        return Color.TRANSPARENT;
    }
}

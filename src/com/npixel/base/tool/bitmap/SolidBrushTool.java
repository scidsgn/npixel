package com.npixel.base.tool.bitmap;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.gui.icons.Icons;
import javafx.scene.image.Image;

public class SolidBrushTool extends BrushBaseTool {
    public SolidBrushTool(Node target, Bitmap bitmap) {
        super(target, bitmap);
    }

    @Override
    public String getName() {
        return "Brush";
    }

    @Override
    public Image getIcon() {
        return Icons.getIcon("brush");
    }

    @Override
    protected Color getColor(int x, int y) {
        return target.getDocument().getForegroundColor();
    }
}

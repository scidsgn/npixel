package com.npixel.base.tool.bitmap;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.properties.IntProperty;
import com.npixel.base.properties.PropUtil;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.gui.icons.Icons;
import javafx.scene.image.Image;

public class BrushBaseTool extends BitmapBaseTool {
    public BrushBaseTool(Node target, Bitmap bitmap) {
        super(target, bitmap);

        propertyGroups.add(new PropertyGroup(
                "brush", "Brush",
                new IntProperty(this, "radius", "Radius", 1, 1, 10)
        ));
    }

    protected Color getColor(int x, int y) {
        return new Color();
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
    public boolean onMouseDragged(double endX, double endY, double movementX, double movementY) {
        double startX = endX - movementX;
        double startY = endY - movementY;

        double maxDelta = Math.ceil(Math.hypot(movementX, movementY));

        IntProperty radiusProp = (IntProperty) PropUtil.getProperty(this, "brush", "radius");
        int radius = (int) radiusProp.getValue();

        for (int i = 0; i < maxDelta; i++) {
            double x = 1 - ((double) i / maxDelta);

            for (int dx = -radius + 1; dx < radius; dx++) {
                for (int dy = -radius + 1; dy < radius; dy++) {
                    int bx = (int) (startX + movementX * x + dx);
                    int by = (int) (startY + movementY * x + dy);
                    bitmap.setPixel(bx, by, getColor(bx, by));
                }
            }
        }

        return false;
    }

    @Override
    public boolean onMouseReleased(double x, double y) {
        return true;
    }
}

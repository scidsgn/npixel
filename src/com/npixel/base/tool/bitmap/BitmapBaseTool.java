package com.npixel.base.tool.bitmap;

import com.npixel.base.Document;
import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.node.Node;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.tool.ITool;
import com.npixel.gui.icons.Icons;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class BitmapBaseTool implements ITool {
    protected Bitmap bitmap;
    protected Node target;
    protected final List<PropertyGroup> propertyGroups;

    public BitmapBaseTool(Node target, Bitmap bitmap) {
        this.bitmap = bitmap;
        this.target = target;

        propertyGroups = new ArrayList<>();
    }

    public Document getDocument() {
        return target.getDocument();
    }

    public String getName() {
        return "";
    }

    public Image getIcon() {
        return Icons.getIcon("none");
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean onMouseDragged(double endX, double endY, double movementX, double movementY) {
        return false;
    }

    public boolean onMousePressed(double x, double y) {
        return false;
    }

    public boolean onMouseReleased(double x, double y) {
        return false;
    }

    public void update() {
    }

    public List<PropertyGroup> getPropertyGroups() {
        return propertyGroups;
    }
}

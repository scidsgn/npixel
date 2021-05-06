package com.npixel.base;

import com.npixel.base.bitmap.Color;
import com.npixel.base.tree.NodeTree;

public class Document {
    private int width;
    private int height;
    private NodeTree tree;

    private Color foregroundColor = new Color();
    private Color backgroundColor = new Color(1, 1, 1);

    public Document(int width, int height) {
        this.width = width;
        this.height = height;

        tree = new NodeTree(this);
    }

    public NodeTree getTree() {
        return tree;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}

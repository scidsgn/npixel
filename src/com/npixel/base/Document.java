package com.npixel.base;

import com.npixel.base.tree.NodeTree;

public class Document {
    private int width;
    private int height;
    private NodeTree tree;

    public Document(int width, int height) {
        this.width = width;
        this.height = height;

        tree = new NodeTree();
    }

    public NodeTree getTree() {
        return tree;
    }
}

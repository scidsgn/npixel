package com.npixel.base.tree;

import com.npixel.base.IBaseType;
import com.npixel.base.node.NodeSocket;

public class NodeConnection {
    private NodeSocket<IBaseType> from;
    private NodeSocket<IBaseType> to;

    NodeConnection(NodeSocket<IBaseType> from, NodeSocket<IBaseType> to) {
        this.from = from;
        this.to = to;
    }

    public NodeSocket<IBaseType> getOutputSocket() {
        return from;
    }

    public NodeSocket<IBaseType> getInputSocket() {
        return to;
    }
}

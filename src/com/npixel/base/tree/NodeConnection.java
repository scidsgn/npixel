package com.npixel.base.tree;

import com.npixel.base.node.NodeSocket;

public class NodeConnection {
    private NodeSocket from;
    private NodeSocket to;

    public NodeConnection(NodeSocket from, NodeSocket to) {
        this.from = from;
        this.to = to;
    }

    public NodeSocket getOutputSocket() {
        return from;
    }

    public NodeSocket getInputSocket() {
        return to;
    }
}

package com.npixel.base.node;

import com.npixel.base.IBaseType;
import com.npixel.base.tree.NodeTree;

import java.util.ArrayList;
import java.util.List;

public class Node {
    protected List<NodeSocket> inputs;
    protected List<NodeSocket> outputs;
    protected NodeTree tree;

    public Node(NodeTree tree) {
        this.tree = tree;

        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
    }

    public NodeSocket getOutput(String id) {
        for (NodeSocket socket : outputs) {
            if (socket.getId().equals(id)) {
                return socket;
            }
        }

        return null;
    }

    public NodeSocket getInput(String id) {
        for (NodeSocket socket : inputs) {
            if (socket.getId().equals(id)) {
                return socket;
            }
        }

        return null;
    }

    protected Object getInputValue(String id) {
        NodeSocket inputSocket = getInput(id);
        if (inputSocket == null) {
            return null;
        }

        NodeSocket connectedSocket = tree.getConnectedOutput(inputSocket);
        if (connectedSocket != null) {
            return connectedSocket.getValue();
        }

        return inputSocket.getValue();
    }

    public void process() {}
}

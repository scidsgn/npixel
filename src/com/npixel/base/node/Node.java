package com.npixel.base.node;

import com.npixel.base.IBaseType;
import com.npixel.base.tree.NodeTree;

import java.util.List;

public class Node {
    private List<NodeSocket<IBaseType>> inputs;
    private List<NodeSocket<IBaseType>> outputs;
    private NodeTree tree;

    Node(NodeTree tree) {
        this.tree = tree;
    }

    public NodeSocket<IBaseType> getOutput(String id) {
        for (NodeSocket<IBaseType> socket : outputs) {
            if (socket.getId().equals(id)) {
                return socket;
            }
        }

        return null;
    }

    public NodeSocket<IBaseType> getInput(String id) {
        for (NodeSocket<IBaseType> socket : inputs) {
            if (socket.getId().equals(id)) {
                return socket;
            }
        }

        return null;
    }

    private IBaseType getInputValue(String id) {
        NodeSocket<IBaseType> inputSocket = getInput(id);
        if (inputSocket == null) {
            return null;
        }

        NodeSocket<IBaseType> connectedSocket = tree.getConnectedOutput(inputSocket);
        if (connectedSocket != null) {
            return connectedSocket.getValue();
        }

        return inputSocket.getValue();
    }
}

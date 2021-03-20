package com.npixel.base.tree;

import com.npixel.base.IBaseType;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;

import java.util.ArrayList;
import java.util.List;

public class NodeTree {
    private List<Node> nodes;
    private List<NodeConnection> connections;

    NodeTree() {
        nodes = new ArrayList<>();
        connections = new ArrayList<>();
    }

    public boolean isNodeInTree(Node node) {
        return nodes.contains(node);
    }

    public NodeSocket<IBaseType> getConnectedOutput(NodeSocket<IBaseType> inputSocket) {
        for (NodeConnection conn : connections) {
            if (conn.getInputSocket() == inputSocket) {
                return conn.getOutputSocket();
            }
        }

        return null;
    }

    public NodeConnection connect(Node fromNode, String fromOutputId, Node toNode, String toInputId) {
        if (!isNodeInTree(fromNode) || !isNodeInTree(toNode)) {
            throw new IllegalArgumentException("Both nodes must belong to the tree.");
        }

        NodeSocket<IBaseType> outputSocket = fromNode.getOutput(fromOutputId);
        NodeSocket<IBaseType> inputSocket = toNode.getInput(toInputId);
        if (inputSocket == null || outputSocket == null) {
            throw new NullPointerException("Socket IDs must be valid.");
        }

        if (getConnectedOutput(inputSocket) != null) {
            // todo: replace connection
        }
        // todo: prevent double connections

        NodeConnection connection = new NodeConnection(outputSocket, inputSocket);
        connections.add(connection);

        return connection;
    }
}

package com.npixel.base.tree;

import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;

import java.util.ArrayList;
import java.util.List;

public class NodeTree {
    private final List<Node> nodes;
    private final List<NodeConnection> connections;

    public NodeTree() {
        nodes = new ArrayList<>();
        connections = new ArrayList<>();
    }

    public void addNode(Node node) {
        if (nodes.contains(node)) {
            throw new IllegalArgumentException("Node already in the node tree.");
        }

        nodes.add(node);
    }

    public void deleteNode(Node node) {
        if (!nodes.contains(node)) {
            throw new IllegalArgumentException("Node must belong to the node tree.");
        }

        disconnectAll(node);
        nodes.remove(node);
    }

    public NodeSocket getConnectedOutput(NodeSocket inputSocket) {
        for (NodeConnection conn : connections) {
            if (conn.getInputSocket() == inputSocket) {
                return conn.getOutputSocket();
            }
        }

        return null;
    }

    private NodeConnection findConnection(NodeSocket fromSocket, NodeSocket toSocket) {
        for (NodeConnection conn : connections) {
            if (conn.getInputSocket() == toSocket && conn.getOutputSocket() == fromSocket) {
                return conn;
            }
        }

        return null;
    }

    private NodeConnection findConnection(NodeSocket toSocket) {
        for (NodeConnection conn : connections) {
            if (conn.getInputSocket() == toSocket) {
                return conn;
            }
        }

        return null;
    }

    private List<NodeConnection> findConnections(NodeSocket socket) {
        List<NodeConnection> list = new ArrayList<>();

        for (NodeConnection conn : connections) {
            if (socket.getType() == NodeSocketType.INPUT && conn.getInputSocket() == socket) {
                list.add(conn);
            } else if (socket.getType() == NodeSocketType.OUTPUT && conn.getOutputSocket() == socket) {
                list.add(conn);
            }
        }

        return list;
    }

    public void disconnectAll(Node node) {
        for (NodeSocket input : node.getInputs()) {
            for (NodeConnection conn : findConnections(input)) {
                connections.remove(conn);
            }
        }

        for (NodeSocket output : node.getOutputs()) {
            for (NodeConnection conn : findConnections(output)) {
                connections.remove(conn);
            }
        }
    }

    public NodeConnection connect(Node fromNode, String fromOutputId, Node toNode, String toInputId) {
        if (!nodes.contains(fromNode) || !nodes.contains(toNode)) {
            throw new IllegalArgumentException("Both nodes must belong to the tree.");
        }

        NodeSocket outputSocket = fromNode.getOutput(fromOutputId);
        NodeSocket inputSocket = toNode.getInput(toInputId);
        if (inputSocket == null || outputSocket == null) {
            throw new NullPointerException("Socket IDs must be valid.");
        }

        NodeConnection toInputConnection = findConnection(inputSocket);
        if (toInputConnection != null) {
            connections.remove(toInputConnection);
        }

        NodeConnection existingConnection = findConnection(outputSocket, inputSocket);
        if (existingConnection != null) {
            connections.remove(existingConnection);
        }

        NodeConnection connection = new NodeConnection(outputSocket, inputSocket);
        connections.add(connection);

        return connection;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<NodeConnection> getConnections() {
        return connections;
    }
}

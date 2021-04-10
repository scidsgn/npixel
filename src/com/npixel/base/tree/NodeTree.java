package com.npixel.base.tree;

import com.npixel.base.events.SimpleEventEmitter;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeCycleColor;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;

import java.util.ArrayList;
import java.util.List;

public class NodeTree extends SimpleEventEmitter<NodeTreeEvent, Node> {
    private final List<Node> nodes;
    private final List<NodeConnection> connections;

    private Node activeNode = null;

    private int updateTick = 0;

    private boolean isInvalid = false;

    public NodeTree() {
        super();

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

    public void smartDeleteNode(Node node) {
        if (!nodes.contains(node)) {
            throw new IllegalArgumentException("Node must belong to the node tree.");
        }

        for (NodeSocket outputSocket : node.getOutputs()) {
            List<NodeConnection> outputConnections = findConnectionsFrom(outputSocket);
            if (connections.size() == 0) {
                continue;
            }

            NodeSocket newOutputSocket = null;
            String ghostId = outputSocket.getGhostConnectedId();
            if (ghostId != null) {
                NodeSocket inputSocket = node.getInput(ghostId);
                if (inputSocket != null) {
                    newOutputSocket = getConnectedOutput(inputSocket);
                }
            }

            for (NodeConnection conn : outputConnections) {
                connections.remove(conn);
                if (newOutputSocket != null) {
                    connections.add(new NodeConnection(newOutputSocket, conn.getInputSocket()));
                    conn.getInputSocket().getParentNode().resetUpdateTick();
                }
            }
        }

        for (NodeSocket inputSocket : node.getInputs()) {
            NodeSocket connectedOutput = getConnectedOutput(inputSocket);
            if (connectedOutput != null) {
                disconnect(connectedOutput, inputSocket);
            }
        }

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

    private List<NodeConnection> findConnectionsFrom(NodeSocket outputSocket) {
        List<NodeConnection> list = new ArrayList<>();

        for (NodeConnection conn : connections) {
            if (conn.getOutputSocket() == outputSocket) {
                list.add(conn);
            }
        }

        return list;
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
                conn.getInputSocket().getParentNode().resetUpdateTick();
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

        inputSocket.getParentNode().resetUpdateTick();

        return connection;
    }

    public void disconnect(NodeSocket fromOutput, NodeSocket toInput) {
        NodeConnection connection = findConnection(fromOutput, toInput);
        if (connection == null) {
            throw new NullPointerException("Sockets must be connected.");
        }

        connections.remove(connection);
        toInput.getParentNode().resetUpdateTick();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<NodeConnection> getConnections() {
        return connections;
    }

    public void bringToFront(Node node) {
        if (!nodes.contains(node)) {
            throw new IllegalArgumentException("Node must belong to the tree.");
        }

        if (nodes.indexOf(node) == nodes.size() - 1) {
            return;
        }

        nodes.remove(node);
        nodes.add(node);
    }

    public Node getActiveNode() {
        return activeNode;
    }

    public void setActiveNode(Node node) {
        if (!nodes.contains(node) && node != null) {
            throw new IllegalArgumentException("Node must belong to the tree.");
        }

        if (node != null) {
            updateSubtree(node);
        }

        this.activeNode = node;
        this.emit(NodeTreeEvent.ACTIVENODECHANGED, this.activeNode);
    }

    public int incrementUpdateTick() {
        updateTick += 1;
        return updateTick;
    }

    private void traverseUpdateTree(List<Node> list, Node node) {
        List<Node> nextList = new ArrayList<>();

        for (NodeSocket input : node.getInputs()) {
            NodeSocket connectedOutput = getConnectedOutput(input);

            if (connectedOutput != null) {
                Node connectedNode = connectedOutput.getParentNode();

                if (connectedNode.requiresUpdate()) {
                    nextList.add(connectedNode);

                    list.remove(connectedNode);
                    list.add(0, connectedNode);
                }
            }
        }

        for (Node nextNode : nextList) {
            traverseUpdateTree(list, nextNode);
        }
    }

    public List<Node> getUpdateOrder(Node startNode) {
        if (!nodes.contains(startNode) || startNode == null) {
            throw new IllegalArgumentException("Node must belong to the tree.");
        }

        List<Node> list = new ArrayList<>();
        if (!startNode.requiresUpdate()) {
            return list;
        }

        list.add(0, startNode);
        traverseUpdateTree(list, startNode);

        return list;
    }

    public void updateSubtree(Node targetNode) {
        if (!nodes.contains(targetNode) || targetNode == null) {
            throw new IllegalArgumentException("Node must belong to the tree.");
        }

        if (checkSubtreeLoops(targetNode)) {
            return;
        }

        for (Node node : getUpdateOrder(targetNode)) {
            node.process();
        }
    }

    private boolean checkSubtreeLoopsIteration(Node node) {
        boolean foundCycle = false;

        node.setCycleColor(NodeCycleColor.PROCESSING);

        for (NodeSocket input : node.getInputs()) {
            NodeSocket connectedOutput = getConnectedOutput(input);
            if (connectedOutput == null) {
                continue;
            }

            Node connectedNode = connectedOutput.getParentNode();
            if (connectedNode.getCycleColor() == NodeCycleColor.PROCESSING) {
                return true;
            }

            foundCycle |= checkSubtreeLoopsIteration(connectedNode);
        }

        node.setCycleColor(NodeCycleColor.COMPLETE);

        return foundCycle;
    }

    public boolean checkSubtreeLoops(Node targetNode) {
        for (Node node : nodes) {
            node.setCycleColor(NodeCycleColor.NONE);
        }

        isInvalid = checkSubtreeLoopsIteration(targetNode);
        return isInvalid;
    }

    public boolean isInvalid() {
        return isInvalid;
    }
}

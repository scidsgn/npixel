package com.npixel.base.node;

import com.npixel.base.tree.NodeTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {
    protected List<NodeSocket> inputs;
    protected List<NodeSocket> outputs;
    protected NodeTree tree;

    protected String typeString = "";
    protected String name;

    private double x = 0.0;
    private double y = 0.0;

    public Node(NodeTree tree) {
        this.tree = tree;

        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
    }

    public List<NodeSocket> getInputs() {
        return inputs;
    }

    public List<NodeSocket> getOutputs() {
        return outputs;
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

        return Objects.requireNonNullElse(connectedSocket, inputSocket).getValue();
    }

    public void process() {}

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getTypeString() {
        return typeString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

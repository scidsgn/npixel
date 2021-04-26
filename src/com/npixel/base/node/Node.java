package com.npixel.base.node;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.events.SimpleEventEmitter;
import com.npixel.base.node.properties.INodeProperty;
import com.npixel.base.node.properties.NodePropertyGroup;
import com.npixel.base.tool.ITool;
import com.npixel.base.tree.NodeTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node extends SimpleEventEmitter<NodeEvent, Node> {
    protected List<NodeSocket> inputs;
    protected List<NodeSocket> outputs;
    protected NodeTree tree;

    protected List<ITool> tools;
    private ITool activeTool = null;

    protected String typeString = "";
    protected String name = null;

    protected List<NodePropertyGroup> propertyGroups;

    private int lastUpdateTick = 0;

    private double x = 0.0;
    private double y = 0.0;

    private NodeCycleColor cycleColor = NodeCycleColor.NONE;

    public Node(NodeTree tree) {
        this.tree = tree;

        propertyGroups = new ArrayList<>();

        inputs = new ArrayList<>();
        outputs = new ArrayList<>();

        tools = new ArrayList<>();
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

    public void process() {
        emit(NodeEvent.UPDATED, this);
        lastUpdateTick = tree.incrementUpdateTick();
    }

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
        if (name == null) {
            return typeString;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
        emit(NodeEvent.APPEARANCEUPDATED, this);
    }

    public int getLastUpdateTick() {
        return lastUpdateTick;
    }

    public void resetUpdateTick() {
        lastUpdateTick = 0;
    }

    public boolean requiresUpdate() {
        if (lastUpdateTick == 0) {
            return true;
        }

        for (NodeSocket input : inputs) {
            NodeSocket outputSocket = tree.getConnectedOutput(input);
            if (
                    outputSocket != null && (
                            outputSocket.getParentNode().getLastUpdateTick() >= lastUpdateTick ||
                            outputSocket.getParentNode().requiresUpdate()
                    )
            ) {
                 return true;
            }
        }

        return false;
    }

    public NodeCycleColor getCycleColor() {
        return cycleColor;
    }

    public void setCycleColor(NodeCycleColor cycleColor) {
        this.cycleColor = cycleColor;
    }

    public Bitmap getThumbnail() {
        return null;
    }

    public List<NodePropertyGroup> getPropertyGroups() {
        return propertyGroups;
    }

    protected INodeProperty getProperty(String groupId, String propertyId) {
        for (NodePropertyGroup propertyGroup : propertyGroups) {
            if (!propertyGroup.getId().equals(groupId)) {
                continue;
            }

            for (INodeProperty property : propertyGroup.getProperties()) {
                if (property.getId().equals(propertyId)) {
                    return property;
                }
            }
        }

        return null;
    }

    public List<ITool> getTools() {
        return tools;
    }

    public ITool getActiveTool() {
        return activeTool;
    }

    public void setActiveTool(ITool tool) {
        if (tools.contains(tool)) {
            activeTool = tool;
        }
    }
}

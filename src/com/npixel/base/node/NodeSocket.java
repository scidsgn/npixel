package com.npixel.base.node;

public class NodeSocket {
    private final String id;
    private String name;
    private final Node parent;
    private final NodeSocketType type;
    private Object value;
    private String ghostConnectedId;

    public NodeSocket(Node parent, String id, NodeSocketType type, String name, Object value) {
        this.parent = parent;
        this.id = id;
        this.type = type;
        this.name = name;
        this.value = value;
        this.ghostConnectedId = null;
    }

    public NodeSocket(Node parent, String id, NodeSocketType type, String name, Object value, String ghostConnectedId) {
        this.parent = parent;
        this.id = id;
        this.type = type;
        this.name = name;
        this.value = value;
        this.ghostConnectedId = ghostConnectedId;
    }

    public String getId() {
        return id;
    }

    public NodeSocketType getType() {
        return type;
    }

    public Node getParentNode() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getGhostConnectedId() {
        return ghostConnectedId;
    }
}

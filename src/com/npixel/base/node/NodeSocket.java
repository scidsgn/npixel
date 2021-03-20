package com.npixel.base.node;

public class NodeSocket {
    private String id;
    private String name;
    private Node parent;
    private NodeSocketType type;
    private Object value;

    NodeSocket(Node parent, String id, NodeSocketType type, String name, Object value) {
        this.parent = parent;
        this.id = id;
        this.type = type;
        this.name = name;
        this.value = value;
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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

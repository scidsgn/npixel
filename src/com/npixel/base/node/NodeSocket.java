package com.npixel.base.node;

public class NodeSocket<T> {
    private String id;
    private String name;
    private Node parent;
    private NodeSocketType type;
    private T value;

    NodeSocket(Node parent, String id, NodeSocketType type, String name, T value) {
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

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}

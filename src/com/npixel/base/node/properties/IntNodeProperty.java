package com.npixel.base.node.properties;

import com.npixel.base.node.Node;

public class IntNodeProperty implements INodeProperty {
    private final int minValue, maxValue;
    private int value;
    private final String name, id;

    private final Node node;

    public IntNodeProperty(Node node, String id, String name, int value, int minValue, int maxValue) {
        this.node = node;

        this.minValue = minValue;
        this.maxValue = maxValue;

        this.value = value;
        this.name = name;
        this.id = id;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;

        node.process();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public Node getNode() {
        return node;
    }
}

package com.npixel.base.node.properties;

import com.npixel.base.node.Node;

public class DoubleNodeProperty implements INodeProperty {
    private final double minValue, maxValue;
    private double value;
    private final String name, id;

    private final Node node;

    public DoubleNodeProperty(Node node, String id, String name, double value, double minValue, double maxValue) {
        this.node = node;

        this.minValue = minValue;
        this.maxValue = maxValue;

        this.value = value;
        this.name = name;
        this.id = id;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;

        node.process();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Node getNode() {
        return node;
    }

    public boolean isCompact() {
        return false;
    }
}

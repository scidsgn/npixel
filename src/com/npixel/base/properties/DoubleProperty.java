package com.npixel.base.properties;

import com.npixel.base.node.Node;

public class DoubleProperty implements IProperty {
    private final double minValue, maxValue;
    private double value;
    private final String name, id;

    private final IUpdateable target;

    public DoubleProperty(IUpdateable target, String id, String name, double value, double minValue, double maxValue) {
        this.target = target;

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

        target.update();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public IUpdateable getTargetObject() {
        return target;
    }

    public boolean isCompact() {
        return false;
    }
}

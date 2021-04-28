package com.npixel.base.properties;

import com.npixel.base.node.Node;

public class IntProperty implements IProperty {
    private final int minValue, maxValue;
    private int value;
    private final String name, id;

    private final IUpdateable target;

    public IntProperty(IUpdateable target, String id, String name, int value, int minValue, int maxValue) {
        this.target = target;

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

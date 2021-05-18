package com.npixel.base.properties;

import com.npixel.base.Vector;

public class VectorProperty implements IProperty {
    private Vector value;
    private final String name, id;

    private final IUpdateable target;

    public VectorProperty(IUpdateable target, String id, String name, Vector value) {
        this.target = target;

        this.value = value;
        this.name = name;
        this.id = id;
    }

    public Vector getValue() {
        return value;
    }

    public void setValue(Vector value) {
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

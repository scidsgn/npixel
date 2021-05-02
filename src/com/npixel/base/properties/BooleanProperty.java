package com.npixel.base.properties;

import com.npixel.base.bitmap.Color;

public class BooleanProperty implements IProperty {
    private boolean value;
    private final String name, id;

    private final IUpdateable target;

    public BooleanProperty(IUpdateable target, String id, String name, boolean value) {
        this.target = target;

        this.value = value;
        this.name = name;
        this.id = id;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
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
        return true;
    }
}

package com.npixel.base.properties;

import com.npixel.base.bitmap.Color;

public class ColorProperty implements IProperty {
    private Color value;
    private final String name, id;

    private final IUpdateable target;

    public ColorProperty(IUpdateable target, String id, String name, Color value) {
        this.target = target;

        this.value = value;
        this.name = name;
        this.id = id;
    }

    public Color getValue() {
        return value;
    }

    public void setValue(Color value) {
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

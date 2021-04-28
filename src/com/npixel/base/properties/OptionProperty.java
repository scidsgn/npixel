package com.npixel.base.properties;

import com.npixel.base.node.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionProperty implements IProperty {
    private final List<String> values;
    private int value;
    private final String name, id;

    private final IUpdateable target;

    public OptionProperty(IUpdateable target, String id, String name, int value, String ...values) {
        this.target = target;

        this.value = value;
        this.name = name;
        this.id = id;

        this.values = new ArrayList<>();
        this.values.addAll(Arrays.asList(values));
    }

    public List<String> getValues() {
        return values;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = Math.max(0, Math.min(values.size() - 1, value));

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

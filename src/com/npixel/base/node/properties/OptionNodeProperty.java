package com.npixel.base.node.properties;

import com.npixel.base.node.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionNodeProperty implements INodeProperty {
    private final List<String> values;
    private int value;
    private final String name, id;

    private final Node node;

    public OptionNodeProperty(Node node, String id, String name, int value, String ...values) {
        this.node = node;

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
        return true;
    }
}

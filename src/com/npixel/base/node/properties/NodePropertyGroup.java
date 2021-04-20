package com.npixel.base.node.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodePropertyGroup {
    private final List<INodeProperty> properties;
    private String name, id;

    public NodePropertyGroup(String id, String name, INodeProperty... properties) {
        this.name = name;
        this.id = id;

        this.properties = new ArrayList<>();
        this.properties.addAll(Arrays.asList(properties));
    }

    public List<INodeProperty> getProperties() {
        return properties;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}

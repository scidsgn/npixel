package com.npixel.base.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyGroup {
    private final List<IProperty> properties;
    private final String name, id;

    public PropertyGroup(String id, String name, IProperty... properties) {
        this.name = name;
        this.id = id;

        this.properties = new ArrayList<>();
        this.properties.addAll(Arrays.asList(properties));
    }

    public List<IProperty> getProperties() {
        return properties;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public IProperty getProperty(String id) {
        for (IProperty property : properties) {
            if (property.getId().equals(id)) {
                return property;
            }
        }

        return null;
    }
}

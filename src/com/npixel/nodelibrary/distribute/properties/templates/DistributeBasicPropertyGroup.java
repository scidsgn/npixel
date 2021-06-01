package com.npixel.nodelibrary.distribute.properties.templates;

import com.npixel.base.properties.IUpdateable;
import com.npixel.base.properties.IntProperty;
import com.npixel.base.properties.PropUtil;
import com.npixel.base.properties.PropertyGroup;

public class DistributeBasicPropertyGroup extends PropertyGroup {
    public DistributeBasicPropertyGroup(IUpdateable target, String id, String name, int n, int seed) {
        super(
                id, name,
                new IntProperty(target, "n", "Scatter count", n, 1, 50),
                new IntProperty(target, "seed", "Seed", seed, 0, 1023)
        );
    }

    public static int getN(IUpdateable target, String groupId) {
        PropertyGroup group = PropUtil.getPropertyGroup(target, groupId);

        if (group instanceof DistributeBasicPropertyGroup) {
            return ((IntProperty)group.getProperty("n")).getValue();
        }

        return -1;
    }

    public static int getSeed(IUpdateable target, String groupId) {
        PropertyGroup group = PropUtil.getPropertyGroup(target, groupId);

        if (group instanceof DistributeBasicPropertyGroup) {
            return ((IntProperty)group.getProperty("seed")).getValue();
        }

        return -1;
    }
}

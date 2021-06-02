package com.npixel.nodelibrary.distribute.properties.templates;

import com.npixel.base.properties.*;

public class DistributeSortPropertyGroup extends PropertyGroup {
    public DistributeSortPropertyGroup(IUpdateable target, String id, String name, int sortX, int sortY) {
        super(
                id, name,
                new OptionProperty(
                        target, "x", "Left-right sorting", 0,
                        "Don't sort", "Left on top", "Right on top"
                ),
                new OptionProperty(
                        target, "y", "Up-down sorting", 0,
                        "Don't sort", "Up on top", "Down on top"
                )
        );
    }

    public static int getSortX(IUpdateable target, String groupId) {
        PropertyGroup group = PropUtil.getPropertyGroup(target, groupId);

        if (group instanceof DistributeSortPropertyGroup) {
            return ((OptionProperty)group.getProperty("x")).getValue();
        }

        return -1;
    }

    public static int getSortY(IUpdateable target, String groupId) {
        PropertyGroup group = PropUtil.getPropertyGroup(target, groupId);

        if (group instanceof DistributeSortPropertyGroup) {
            return ((OptionProperty)group.getProperty("y")).getValue();
        }

        return -1;
    }
}

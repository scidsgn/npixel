package com.npixel.base.properties.templates;

import com.npixel.base.properties.*;

public class SizePropertyGroup extends PropertyGroup {
    public SizePropertyGroup(IUpdateable target, String id, String name, int width, int height, int minSize, int maxSize) {
        super(
                id, name,
                new BooleanProperty(target, "uniform", "1:1 ratio (use only Width)", false),
                new IntProperty(target, "width", "Width", width, minSize, maxSize),
                new IntProperty(target, "height", "Height", height, minSize, maxSize)
        );
    }

    public static int getWidth(IUpdateable target, String groupId) {
        PropertyGroup group = PropUtil.getPropertyGroup(target, groupId);

        if (group instanceof SizePropertyGroup) {
            return ((IntProperty)group.getProperty("width")).getValue();
        }

        return -1;
    }

    public static int getHeight(IUpdateable target, String groupId) {
        PropertyGroup group = PropUtil.getPropertyGroup(target, groupId);

        if (group instanceof SizePropertyGroup) {
            boolean isUniform = ((BooleanProperty)group.getProperty("uniform")).getValue();

            return isUniform ?
                    ((IntProperty)group.getProperty("width")).getValue() :
                    ((IntProperty)group.getProperty("height")).getValue();
        }

        return -1;
    }
}

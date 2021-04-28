package com.npixel.base.properties;

import com.npixel.base.node.Node;

public interface IProperty {
    String getId();
    String getName();

    boolean isCompact();

    IUpdateable getTargetObject();
    // TODO: serialize
}

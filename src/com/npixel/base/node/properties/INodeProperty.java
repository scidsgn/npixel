package com.npixel.base.node.properties;

import com.npixel.base.node.Node;

public interface INodeProperty {
    String getId();
    String getName();

    boolean isCompact();

    Node getNode();
    // TODO: serialize
}

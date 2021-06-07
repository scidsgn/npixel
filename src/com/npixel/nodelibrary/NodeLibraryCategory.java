package com.npixel.nodelibrary;

import com.npixel.base.node.Node;
import com.npixel.base.tree.NodeTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class NodeLibraryCategory {
    private final String name;
    private final List<NodeLibraryNode> nodes;
    private boolean beforeSeparator;

    @SafeVarargs
    public NodeLibraryCategory(String name, NodeLibraryNode ...nodes) {
        this.name = name;
        this.nodes = new ArrayList<>();
        Collections.addAll(this.nodes, nodes);

        this.beforeSeparator = false;
    }

    @SafeVarargs
    public NodeLibraryCategory(String name,boolean beforeSeparator, NodeLibraryNode ...nodes) {
        this(name, nodes);

        this.beforeSeparator = beforeSeparator;
    }

    public String getName() {
        return name;
    }

    public List<NodeLibraryNode> getNodes() {
        return nodes;
    }

    public boolean isBeforeSeparator() {
        return beforeSeparator;
    }
}

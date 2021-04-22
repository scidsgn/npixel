package com.npixel.nodelibrary;

import com.npixel.base.node.Node;
import com.npixel.base.tree.NodeTree;

import java.util.function.Function;

public class NodeLibraryNode {
    private final Function<NodeTree, Node> nodeCreator;
    private final String id, name;

    public NodeLibraryNode(String id, String name, Function<NodeTree, Node> nodeCreator) {
        this.nodeCreator = nodeCreator;

        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Node create(NodeTree tree) {
        return nodeCreator.apply(tree);
    }
}

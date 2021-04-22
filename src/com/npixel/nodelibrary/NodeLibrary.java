package com.npixel.nodelibrary;

import com.npixel.base.node.Node;
import com.npixel.base.tree.NodeTree;
import com.npixel.nodelibrary.color.ColorCrossfadeNode;

import java.util.ArrayList;
import java.util.List;

public class NodeLibrary {
    private final List<NodeLibraryCategory> categories;

    private NodeLibrary() {
        this.categories = new ArrayList<>();

        this.categories.add(new NodeLibraryCategory(
                "Color",
                new NodeLibraryNode("ColorXfade", "Crossfade", ColorCrossfadeNode::new)
        ));
    }

    public Node createNodeFromId(NodeTree tree, String id) {
        for (NodeLibraryCategory category : categories) {
            for (NodeLibraryNode node : category.getNodes()) {
                if (node.getId().equals(id)) {
                    return node.create(tree);
                }
            }
        }

        return null;
    }

    public List<NodeLibraryCategory> getCategories() {
        return categories;
    }

    public static NodeLibrary nodeLibrary = new NodeLibrary();
}

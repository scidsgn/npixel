package com.npixel.nodelibrary;

import com.npixel.base.node.Node;
import com.npixel.base.tree.NodeTree;
import com.npixel.nodelibrary.composite.*;
import com.npixel.nodelibrary.shape.*;
import com.npixel.nodelibrary.source.*;

import java.util.ArrayList;
import java.util.List;

public class NodeLibrary {
    private final List<NodeLibraryCategory> categories;

    private NodeLibrary() {
        this.categories = new ArrayList<>();

        this.categories.add(new NodeLibraryCategory(
                "Source",
                new NodeLibraryNode("SourceBitmap", "Pixel Layer", "sourcebitmap", SourceBitmapNode::new)
        ));
        this.categories.add(new NodeLibraryCategory(
                "Shape",
                new NodeLibraryNode("ShapeEllipse", "Ellipse", "shapeellipse", ShapeEllipseNode::new),
                new NodeLibraryNode("ShapeRectangle", "Rectangle", "shaperectangle", ShapeRectangleNode::new),
                new NodeLibraryNode("ShapeRegPolygon", "Regular Polygon", "shaperegpolygon", ShapeRegPolygonNode::new)
        ));
        this.categories.add(new NodeLibraryCategory(
                "Composite",
                new NodeLibraryNode("CompAComp", "Alpha Composite", "compacomp", CompositeAlphaCompositeNode::new),
                new NodeLibraryNode("CompXfade", "Crossfade", "compxfade", CompositeCrossfadeNode::new),
                new NodeLibraryNode("CompMask", "Mask", "compmask", CompositeMaskNode::new)
        ));
    }

    public NodeLibraryNode getNode(String id) {
        for (NodeLibraryCategory category : categories) {
            for (NodeLibraryNode node : category.getNodes()) {
                if (node.getId().equals(id)) {
                    return node;
                }
            }
        }

        return null;
    }

    public Node createNodeFromId(NodeTree tree, String id) {
        NodeLibraryNode node = getNode(id);
        if (node != null) {
            return node.create(tree);
        }

        return null;
    }

    public List<NodeLibraryCategory> getCategories() {
        return categories;
    }

    public static NodeLibrary nodeLibrary = new NodeLibrary();
}

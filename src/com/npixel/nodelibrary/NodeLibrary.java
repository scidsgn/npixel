package com.npixel.nodelibrary;

import com.npixel.base.node.Node;
import com.npixel.base.tree.NodeTree;
import com.npixel.nodelibrary.channel.*;
import com.npixel.nodelibrary.color.*;
import com.npixel.nodelibrary.composite.*;
import com.npixel.nodelibrary.grade.*;
import com.npixel.nodelibrary.p3d.*;
import com.npixel.nodelibrary.shape.*;
import com.npixel.nodelibrary.source.*;
import com.npixel.nodelibrary.texture.*;

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
                "Color",
                new NodeLibraryNode("ColorGrayscale", "Grayscale", "none", ColorGrayscaleNode::new),
                new NodeLibraryNode("ColorInvert", "Invert", "none", ColorInvertNode::new),
                new NodeLibraryNode("ColorThreshold", "Threshold", "none", ColorThresholdNode::new),
                new NodeLibraryNode("ColorDuotone", "Duotone", "none", ColorDuotoneNode::new),
                new NodeLibraryNode("ColorTritone", "Tritone", "none", ColorTritoneNode::new)
        ));
        this.categories.add(new NodeLibraryCategory(
                "Grade",
                new NodeLibraryNode("GradeLevels", "Levels", "none", GradeLevelsNode::new)
        ));
        this.categories.add(new NodeLibraryCategory(
                "Channel",
                new NodeLibraryNode("ChannelSeparate", "Separate RGB", "none", ChannelSeparateNode::new)
        ));
        this.categories.add(new NodeLibraryCategory(
                "Shape",
                new NodeLibraryNode("ShapeEllipse", "Ellipse", "shapeellipse", ShapeEllipseNode::new),
                new NodeLibraryNode("ShapeRectangle", "Rectangle", "shaperectangle", ShapeRectangleNode::new),
                new NodeLibraryNode("ShapeRegPolygon", "Regular Polygon", "shaperegpolygon", ShapeRegPolygonNode::new),
                new NodeLibraryNode("ShapeStar", "Star", "none", ShapeStarNode::new)
        ));
        this.categories.add(new NodeLibraryCategory(
                "Composite",
                new NodeLibraryNode("CompAComp", "Alpha Composite", "compacomp", CompositeAlphaCompositeNode::new),
                new NodeLibraryNode("CompXfade", "Crossfade", "compxfade", CompositeCrossfadeNode::new),
                new NodeLibraryNode("CompMask", "Mask", "compmask", CompositeMaskNode::new)
        ));
        this.categories.add(new NodeLibraryCategory(
                "Texture",
                new NodeLibraryNode("TextureXOR", "XOR Texture", "texturexor", TextureXORNode::new),
                new NodeLibraryNode("TextureNoise", "White Noise Texture", "none", TextureNoiseNode::new)
        ));
        this.categories.add(new NodeLibraryCategory(
                "Pseudo-3D",
                new NodeLibraryNode("P3DNormalMapNode", "Normal Map", "none", P3DNormalMapNode::new)
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

package com.npixel.nodelibrary;

import com.npixel.base.node.Node;
import com.npixel.base.tree.NodeTree;
import com.npixel.nodelibrary.channel.*;
import com.npixel.nodelibrary.color.*;
import com.npixel.nodelibrary.composite.*;
import com.npixel.nodelibrary.distribute.*;
import com.npixel.nodelibrary.fill.*;
import com.npixel.nodelibrary.filter.*;
import com.npixel.nodelibrary.grade.*;
import com.npixel.nodelibrary.p3d.*;
import com.npixel.nodelibrary.shape.*;
import com.npixel.nodelibrary.source.*;
import com.npixel.nodelibrary.texture.*;

import java.util.ArrayList;
import java.util.List;

public class NodeLibrary {
    private final List<NodeLibraryCategory> categories;
    private final List<NodeLibraryNode> singleNodes;

    private NodeLibrary() {
        this.categories = new ArrayList<>();
        this.singleNodes = new ArrayList<>();

        this.singleNodes.add(
                new NodeLibraryNode("SourceBitmap", "Pixel Layer", "sourcebitmap", SourceBitmapNode::new)
        );

        this.categories.add(new NodeLibraryCategory(
                "Shape",
                new NodeLibraryNode("ShapeEllipse", "Ellipse", "shapeellipse", ShapeEllipseNode::new),
                new NodeLibraryNode("ShapeRectangle", "Rectangle", "shaperectangle", ShapeRectangleNode::new),
                new NodeLibraryNode("ShapeRegPolygon", "Regular Polygon", "shaperegpolygon", ShapeRegPolygonNode::new)
        ));

        this.categories.add(new NodeLibraryCategory(
                "Fill",
                new NodeLibraryNode("FillSolid", "Solid color", "fillsolid", FillSolidNode::new),
                new NodeLibraryNode("FillGradient", "Gradient", "fillgradient", FillGradientNode::new)
        ));
        this.categories.add(new NodeLibraryCategory(
                "Distribute",
                new NodeLibraryNode("DistributeScatter", "Scatter", "distributescatter", DistributeScatterNode::new)
        ));
        this.categories.add(new NodeLibraryCategory(
                "Composite", true,
                new NodeLibraryNode("CompAComp", "Alpha Composite", "compacomp", CompositeAlphaCompositeNode::new),
                new NodeLibraryNode("CompXfade", "Crossfade", "compxfade", CompositeCrossfadeNode::new),
                new NodeLibraryNode("CompMask", "Mask", "compmask", CompositeMaskNode::new)
        ));

        this.categories.add(new NodeLibraryCategory(
                "Color",
                new NodeLibraryNode("ColorGrayscale", "Grayscale", "colorgrayscale", ColorGrayscaleNode::new),
                new NodeLibraryNode("ColorInvert", "Invert", "colorinvert", ColorInvertNode::new),
                new NodeLibraryNode("ColorThreshold", "Threshold", "colorthreshold", ColorThresholdNode::new),
                new NodeLibraryNode("ColorDuotone", "Duotone", "colorduotone", ColorDuotoneNode::new),
                new NodeLibraryNode("ColorTritone", "Tritone", "colortritone", ColorTritoneNode::new)
        ));
        this.categories.add(new NodeLibraryCategory(
                "Grade",
                new NodeLibraryNode("GradeLevels", "Levels", "gradelevels", GradeLevelsNode::new),
                new NodeLibraryNode("GradeBrightContrast", "Brightness & Contrast", "gradebrightcontrast", GradeBrightContrastNode::new)
        ));
        this.categories.add(new NodeLibraryCategory(
                "Channel", true,
                new NodeLibraryNode("ChannelSeparate", "Separate RGB", "channelseparate", ChannelSeparateNode::new)
        ));

        this.categories.add(new NodeLibraryCategory(
                "Filter",
                new NodeLibraryNode("FilterEdgeDetect", "Edge Detection", "filteredgedetect", FilterEdgeDetectionNode::new),
                new NodeLibraryNode("FilterSoften", "Soften", "filtersoften", FilterSoftenNode::new)
        ));
        this.categories.add(new NodeLibraryCategory(
                "Texture",
                new NodeLibraryNode("TextureXOR", "XOR Texture", "texturexor", TextureXORNode::new),
                new NodeLibraryNode("TextureNoise", "White Noise Texture", "texturenoise", TextureNoiseNode::new)
        ));
    }

    public NodeLibraryNode getNode(String id) {
        for (NodeLibraryNode node : singleNodes) {
            if (node.getId().equals(id)) {
                return node;
            }
        }

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

    public List<NodeLibraryNode> getSingleNodes() {
        return singleNodes;
    }

    public List<NodeLibraryCategory> getCategories() {
        return categories;
    }

    public static NodeLibrary nodeLibrary = new NodeLibrary();
}

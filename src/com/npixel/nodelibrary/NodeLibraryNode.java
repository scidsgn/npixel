package com.npixel.nodelibrary;

import com.npixel.base.node.Node;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.function.Function;

public class NodeLibraryNode {
    private final Function<NodeTree, Node> nodeCreator;
    private final String id, name;
    private final Image icon;

    public NodeLibraryNode(String id, String name, Function<NodeTree, Node> nodeCreator) {
        this.nodeCreator = nodeCreator;

        this.id = id;
        this.name = name;
        this.icon = Icons.getIcon("none");
    }

    public NodeLibraryNode(String id, String name, String iconName, Function<NodeTree, Node> nodeCreator) {
        this.nodeCreator = nodeCreator;

        this.id = id;
        this.name = name;
        this.icon = Icons.getIcon(iconName);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Image getIcon() {
        return icon;
    }

    public ImageView getIconView() {
        ImageView imageView = new ImageView(icon);
        imageView.setFitHeight(16);
        imageView.setFitWidth(16);

        return imageView;
    }

    public Node create(NodeTree tree) {
        Node node = nodeCreator.apply(tree);
        node.setName(tree.makeUniqueName(name));

        return node;
    }
}

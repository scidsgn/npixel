package com.npixel.gui.nodeeditor;

import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.tree.NodeConnection;
import com.npixel.base.tree.NodeTree;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class NodeEditor extends Canvas {
    private final NodeTree tree;

    public NodeEditor(NodeTree tree, double width, double height) {
        super(width, height);

        this.tree = tree;
    }

    private void renderNode(GraphicsContext ctx, Node node) {
        double xBase = node.getX();
        double yBase = node.getY();

        ctx.setFill(Color.GRAY);
        ctx.fillRect(xBase, yBase, 150, 30);

        ctx.setFill(Color.LIGHTGRAY);
        ctx.fillRect(xBase + 2, yBase + 2, 146, 26);

        ctx.setFill(Color.BLACK);
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setTextAlign(TextAlignment.LEFT);
        ctx.fillText(node.getName(), xBase + 8, yBase + 15);

        ctx.setFill(Color.GRAY);
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setTextAlign(TextAlignment.RIGHT);
        ctx.fillText(node.getTypeString(), xBase + 150 - 8, yBase + 15);

        System.out.println(3);
    }

    private void renderConnection(GraphicsContext ctx, NodeConnection conn) {
        NodeSocket from = conn.getOutputSocket();
        Node fromNode = from.getParentNode();

        NodeSocket to = conn.getInputSocket();
        Node toNode = to.getParentNode();

        ctx.moveTo(fromNode.getX() + 20, fromNode.getY() + 20);
        ctx.lineTo(toNode.getX() + 20, toNode.getY() + 20);
    }

    public void render() {
        GraphicsContext ctx = getGraphicsContext2D();

        ctx.clearRect(0, 0, getWidth(), getHeight());

        ctx.beginPath();
        for (NodeConnection conn : tree.getConnections()) {
            renderConnection(ctx, conn);
        }
        ctx.stroke();

        for (Node n : tree.getNodes()) {
            renderNode(ctx, n);
        }
    }
}

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

        int socketCount = node.getInputs().size() + node.getOutputs().size();

        ctx.setFill(Color.GRAY);
        ctx.fillRect(xBase, yBase, 150, 32 + socketCount * 24);

        ctx.setFill(Color.LIGHTGRAY);
        ctx.fillRect(xBase + 2, yBase + 2, 146, 26);

        ctx.setFill(Color.BLACK);
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setTextAlign(TextAlignment.LEFT);
        ctx.fillText(node.getName(), xBase + 16, yBase + 15);

        ctx.setFill(Color.GRAY);
        ctx.setTextAlign(TextAlignment.RIGHT);
        ctx.fillText(node.getTypeString(), xBase + 150 - 16, yBase + 15);

        ctx.setFill(Color.WHITE);
        ctx.fillRect(xBase + 2, yBase + 30, 146, socketCount * 24);

        int i = 0;

        for (NodeSocket socket : node.getInputs()) {
            int yOffset = 30 + i * 24;

            ctx.setFill(Color.BLACK);
            ctx.setTextAlign(TextAlignment.LEFT);
            ctx.fillText(socket.getName(), xBase + 16, yBase + yOffset + 12);

            ctx.setFill(Color.GRAY);
            ctx.fillRect(xBase, yBase + yOffset + 7, 10, 10);

            ctx.setFill(Color.LIGHTGRAY);
            ctx.fillRect(xBase + 2, yBase + yOffset + 9, 6, 6);

            i++;
        }
        for (NodeSocket socket : node.getOutputs()) {
            int yOffset = 30 + i * 24;

            ctx.setFill(Color.BLACK);
            ctx.setTextAlign(TextAlignment.RIGHT);
            ctx.fillText(socket.getName(), xBase + 150 - 16, yBase + yOffset + 12);

            ctx.setFill(Color.GRAY);
            ctx.fillRect(xBase + 140, yBase + yOffset + 7, 10, 10);

            ctx.setFill(Color.LIGHTGRAY);
            ctx.fillRect(xBase + 142, yBase + yOffset + 9, 6, 6);

            i++;
        }

        System.out.println(3);
    }

    private void renderConnection(GraphicsContext ctx, NodeConnection conn) {
        NodeSocket from = conn.getOutputSocket();
        Node fromNode = from.getParentNode();
        int fromYOffset = 30 + 24 * (fromNode.getInputs().size() + fromNode.getOutputs().indexOf(from));

        NodeSocket to = conn.getInputSocket();
        Node toNode = to.getParentNode();
        int toYOffset = 30 + 24 * toNode.getInputs().indexOf(to);

        ctx.moveTo(fromNode.getX() + 145, fromNode.getY() + fromYOffset + 12);
        ctx.lineTo(toNode.getX() + 5, toNode.getY() + toYOffset + 12);
    }

    public void render() {
        GraphicsContext ctx = getGraphicsContext2D();

        ctx.clearRect(0, 0, getWidth(), getHeight());

        for (Node n : tree.getNodes()) {
            renderNode(ctx, n);
        }

        ctx.beginPath();
        ctx.setStroke(Color.GRAY);
        ctx.setLineWidth(2);
        for (NodeConnection conn : tree.getConnections()) {
            renderConnection(ctx, conn);
        }
        ctx.stroke();
    }
}

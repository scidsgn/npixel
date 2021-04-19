package com.npixel.gui.rastereditor;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeEvent;
import com.npixel.base.node.NodeSocket;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RasterEditor extends Canvas {
    private Node currentNode = null;

    public RasterEditor() {
        widthProperty().addListener(event -> render());
        heightProperty().addListener(event -> render());
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
        setWidth(width);
        setHeight(height);
        render();
    }

    @Override
    public double minWidth(double v) {
        return 1;
    }

    @Override
    public double minHeight(double v) {
        return 1;
    }

    @Override
    public double maxWidth(double v) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double maxHeight(double v) {
        return Double.POSITIVE_INFINITY;
    }

    public void render() {
        GraphicsContext ctx = getGraphicsContext2D();

        ctx.clearRect(0, 0, getWidth(), getHeight());

        if (currentNode != null && currentNode.getOutputs().size() > 0) {
            NodeSocket firstOutput = currentNode.getOutputs().get(0);
            Object v = firstOutput.getValue();

            if (v instanceof Bitmap) {
                Bitmap bmp = (Bitmap) v;

                ctx.setFill(Color.BLACK);
                ctx.fillRect(59, 59, bmp.getWidth() + 2, bmp.getHeight() + 2);

                ctx.drawImage(bmp, 60, 60);
            } else if (v instanceof Integer) {
                ctx.fillText(v.toString(), 50, 50);
            }
        } else {
            ctx.fillText("Null!", 50, 50);
        }
    }

    private Void onNodeUpdated(Node node) {
        render();

        return null;
    }

    public void setCurrentNode(Node currentNode) {
        if (this.currentNode != null) {
            this.currentNode.off(NodeEvent.UPDATED, this::onNodeUpdated);
        }

        this.currentNode = currentNode;
        if (this.currentNode != null) {
            this.currentNode.on(NodeEvent.UPDATED, this::onNodeUpdated);
        }

        render();
    }
}

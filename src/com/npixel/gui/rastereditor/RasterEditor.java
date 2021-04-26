package com.npixel.gui.rastereditor;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeEvent;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.tool.ITool;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class RasterEditor extends Canvas {
    private Node currentNode = null;

    private final Double[] lastMousePosition = {0.0, 0.0};
    double viewX = 0, viewY = 0;

    public RasterEditor() {
        widthProperty().addListener(event -> render());
        heightProperty().addListener(event -> render());

        prepareEvents();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    private void prepareEvents() {
        this.setOnMousePressed(this::handleMouseDown);
        this.setOnMouseReleased(this::handleMouseRelease);
        this.setOnMouseDragged(this::handleDragging);
    }

    private void handleMouseDown(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            ITool tool = currentNode.getActiveTool();
            if (tool != null) {
                boolean update = tool.onMousePressed(
                        mouseEvent.getX() - viewX,
                        mouseEvent.getY() - viewY
                );
                if (update) {
                    currentNode.process();
                }
            }
        }

        lastMousePosition[0] = mouseEvent.getX();
        lastMousePosition[1] = mouseEvent.getY();

        render();
    }

    private void handleMouseRelease(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            ITool tool = currentNode.getActiveTool();
            if (tool != null) {
                boolean update = tool.onMouseReleased(
                        mouseEvent.getX() - viewX,
                        mouseEvent.getY() - viewY
                );
                if (update) {
                    currentNode.process();
                }
            }
        }

        lastMousePosition[0] = mouseEvent.getX();
        lastMousePosition[1] = mouseEvent.getY();

        render();
    }

    private void handleDragging(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            ITool tool = currentNode.getActiveTool();
            if (tool != null) {
                boolean update = tool.onMouseDragged(
                        mouseEvent.getX() - viewX,
                        mouseEvent.getY() - viewY,
                        mouseEvent.getX() - lastMousePosition[0],
                        mouseEvent.getY() - lastMousePosition[1]
                );
                if (update) {
                    currentNode.process();
                }
            }
        } else if (mouseEvent.getButton() == MouseButton.MIDDLE) {
            viewX += mouseEvent.getX() - lastMousePosition[0];
            viewY += mouseEvent.getY() - lastMousePosition[1];
        }

        lastMousePosition[0] = mouseEvent.getX();
        lastMousePosition[1] = mouseEvent.getY();

        render();
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

        ctx.save();
        ctx.translate(viewX, viewY);

        if (currentNode != null && currentNode.getOutputs().size() > 0) {
            NodeSocket firstOutput = currentNode.getOutputs().get(0);
            Object v = firstOutput.getValue();

            if (v instanceof Bitmap) {
                Bitmap bmp = (Bitmap) v;

                ctx.setFill(Color.BLACK);
                ctx.fillRect(-1, -1, bmp.getWidth() + 2, bmp.getHeight() + 2);

                ctx.drawImage(bmp, 0, 0);
            } else if (v instanceof Integer) {
                ctx.fillText(v.toString(), 50, 50);
            }
        } else {
            ctx.fillText("Null!", 50, 50);
        }

        ctx.restore();
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

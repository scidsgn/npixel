package com.npixel.gui.rastereditor;

import com.npixel.base.Document;
import com.npixel.base.ViewportCoordinates;
import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeEvent;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.tool.ITool;
import com.npixel.gui.icons.Icons;
import com.npixel.gui.utils.TransparencyGrid;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class RasterEditor extends Canvas {
    private final Document doc;
    private Node currentNode = null;

    private final Double[] lastMousePosition = {0.0, 0.0};
    private final ViewportCoordinates coordinates;

    private final InternalHandTool handTool = new InternalHandTool(this);

    public RasterEditor(Document doc) {
        this.doc = doc;
        this.coordinates = doc.getCoordinates();

        widthProperty().addListener(event -> render());
        heightProperty().addListener(event -> render());

        prepareEvents();
    }

    public Document getDocument() {
        return doc;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    private void prepareEvents() {
        this.setOnMousePressed(this::handleMouseDown);
        this.setOnMouseReleased(this::handleMouseRelease);
        this.setOnMouseDragged(this::handleDragging);
        this.setOnScroll(this::handleScroll);
    }

    private void handleScroll(ScrollEvent scrollEvent) {
        double[] pos = coordinates.clientToWorld(scrollEvent.getX(), scrollEvent.getY());
        double factor = coordinates.getScaleFactor() + scrollEvent.getDeltaY() / 40;
        factor = Math.max(1.0, factor);

        coordinates.scaleTo(factor, pos[0], pos[1]);

        render();
    }

    private void handleMouseDown(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY && currentNode != null) {
            ITool tool = currentNode.getActiveTool();
            if (tool != null) {
                double[] pos = coordinates.clientToWorld(mouseEvent.getX(), mouseEvent.getY());
                boolean update = tool.onMousePressed(pos[0], pos[1] );
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
        if (mouseEvent.getButton() == MouseButton.PRIMARY && currentNode != null) {
            ITool tool = currentNode.getActiveTool();
            if (tool != null) {
                double[] pos = coordinates.clientToWorld(mouseEvent.getX(), mouseEvent.getY());
                boolean update = tool.onMouseReleased(pos[0], pos[1]);
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
        if (mouseEvent.getButton() == MouseButton.PRIMARY && currentNode != null) {
            ITool tool = currentNode.getActiveTool();
            if (tool != null) {
                double[] pos = coordinates.clientToWorld(mouseEvent.getX(), mouseEvent.getY());
                boolean update = tool.onMouseDragged(
                        pos[0], pos[1],
                        coordinates.deltaClientToWorld(mouseEvent.getX() - lastMousePosition[0]),
                        coordinates.deltaClientToWorld(mouseEvent.getY() - lastMousePosition[1])
                );
                if (update) {
                    currentNode.process();
                }
            }
        } else if (mouseEvent.getButton() == MouseButton.MIDDLE) {
            coordinates.translate(mouseEvent.getX() - lastMousePosition[0], mouseEvent.getY() - lastMousePosition[1]);
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

    public void panView(double dx, double dy) {
        coordinates.translateInWorld(dx, dy);
    }

    public void render() {
        GraphicsContext ctx = getGraphicsContext2D();

        ctx.clearRect(0, 0, getWidth(), getHeight());

        ctx.save();
        ctx.setImageSmoothing(false);
        coordinates.transformContext(ctx);

        if (currentNode != null && currentNode.getOutputs().size() > 0) {
            int xOffset = 0;

            for (NodeSocket output : currentNode.getOutputs()) {
                Object v = output.getValue();

                if (v instanceof Bitmap) {
                    Bitmap bmp = (Bitmap) v;

                    String layerName = String.format("%s (%dx%d)", output.getName(), (int)bmp.getWidth(), (int)bmp.getHeight());
                    ctx.setFill(Color.BLACK);
                    ctx.fillText(layerName, xOffset, -6);

                    ctx.setFill(Color.DARKGRAY);
                    ctx.fillRect(xOffset - 1, -1, bmp.getWidth() + 2, bmp.getHeight() + 2);
                    ctx.setFill(TransparencyGrid.grid.getPattern());
                    ctx.fillRect(xOffset, 0, bmp.getWidth(), bmp.getHeight());

                    ctx.drawImage(bmp, xOffset, 0);

                    xOffset += bmp.getWidth() + 8;
                }
            }
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

    public ITool getHandTool() {
        return handTool;
    }

    private static class InternalHandTool implements ITool {
        private final List<PropertyGroup> propertyGroups;
        private final RasterEditor editor;

        public InternalHandTool(RasterEditor editor) {
            propertyGroups = new ArrayList<>();

            this.editor = editor;
        }

        public Document getDocument() {
            return editor.getDocument();
        }

        public String getName() {
            return "Hand";
        }

        public Image getIcon() {
            return Icons.getIcon("hand");
        }

        public boolean onMouseDragged(double endX, double endY, double movementX, double movementY) {
            editor.panView(movementX, movementY);
            return false;
        }

        public boolean onMousePressed(double x, double y) {
            return false;
        }

        public boolean onMouseReleased(double x, double y) {
            return false;
        }

        public void update() {}

        public List<PropertyGroup> getPropertyGroups() {
            return propertyGroups;
        }
    }
}

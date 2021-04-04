package com.npixel.gui.nodeeditor;

import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.tree.NodeConnection;
import com.npixel.base.tree.NodeTree;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class NodeEditor extends Canvas {
    private final NodeTree tree;
    private NodeSocket activeSocket = null;

    private ContextMenu selectedNodeMenu;

    private final Double[] lastMousePosition = {0.0, 0.0};

    double viewX = 0, viewY = 0;

    public NodeEditor(NodeTree tree) {
        this.tree = tree;

        this.setOnMousePressed(this::handleMouseDown);
        this.setOnMouseReleased(this::handleMouseRelease);
        this.setOnMouseDragged(this::handleDragging);
        this.setOnContextMenuRequested(this::handleContextMenu);

        widthProperty().addListener(event -> render());
        heightProperty().addListener(event -> render());

        setupSelectedNodeMenu();
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

    private void setupSelectedNodeMenu() {
        selectedNodeMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Delete node");
        deleteItem.setOnAction(event -> {
            tree.smartDeleteNode(tree.getActiveNode());
            tree.setActiveNode(null);

            render();
        });
        MenuItem disconnectItem = new MenuItem("Remove all connections");
        disconnectItem.setOnAction(event -> {
            tree.disconnectAll(tree.getActiveNode());
            render();
        });

        selectedNodeMenu.getItems().addAll(deleteItem, disconnectItem);
    }

    private void handleContextMenu(ContextMenuEvent contextMenuEvent) {
        if (tree.getActiveNode() != null) {
            selectedNodeMenu.show(this, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        }
    }

    private void handleMouseDown(MouseEvent mouseEvent) {
        Node node = hitTest(mouseEvent.getX() - viewX, mouseEvent.getY() - viewY);

        selectedNodeMenu.hide();

        if (node != null) {
            double xOffset = mouseEvent.getX() - node.getX() - viewX;

            NodeSocket socket = hitTestSocket(node, mouseEvent.getX() - viewX, mouseEvent.getY() - viewY);
            if (socket != null) {
                if (xOffset >= 140 && socket.getType() == NodeSocketType.OUTPUT) {
                    activeSocket = socket;
                } else if (xOffset < 10 && socket.getType() == NodeSocketType.INPUT) {
                    NodeSocket outputSocket = tree.getConnectedOutput(socket);

                    if (outputSocket != null) {
                        tree.disconnect(outputSocket, socket);
                        activeSocket = outputSocket;
                    } else {
                        activeSocket = socket;
                    }
                }
            }

            tree.setActiveNode(node);
            tree.bringToFront(node);
        } else {
            tree.setActiveNode(null);
        }

        lastMousePosition[0] = mouseEvent.getX();
        lastMousePosition[1] = mouseEvent.getY();

        render();
    }

    private void handleMouseRelease(MouseEvent mouseEvent) {
        if (activeSocket != null) {
            Node node = hitTest(mouseEvent.getX() - viewX, mouseEvent.getY() - viewY);
            NodeSocket targetSocket = hitTestSocket(node, mouseEvent.getX() - viewX, mouseEvent.getY() - viewY);

            if (targetSocket != null) {
                if (node != activeSocket.getParentNode()) {
                    if (
                            activeSocket.getType() == NodeSocketType.INPUT &&
                            targetSocket.getType() == NodeSocketType.OUTPUT
                    ) {
                        tree.connect(node, targetSocket.getId(), activeSocket.getParentNode(), activeSocket.getId());
                    } else if (
                            activeSocket.getType() == NodeSocketType.OUTPUT &&
                            targetSocket.getType() == NodeSocketType.INPUT
                    ) {
                        tree.connect(activeSocket.getParentNode(), activeSocket.getId(), node, targetSocket.getId());
                    }
                }
            }

            activeSocket = null;
        }
        render();
    }

    private void handleDragging(MouseEvent mouseEvent) {
        Node node = hitTest(mouseEvent.getX() - viewX, mouseEvent.getY() - viewY);

        if (mouseEvent.getButton() == MouseButton.PRIMARY && node != null && activeSocket == null) {
            node.setX(node.getX() + mouseEvent.getX() - lastMousePosition[0]);
            node.setY(node.getY() + mouseEvent.getY() - lastMousePosition[1]);
        } else if (mouseEvent.getButton() == MouseButton.MIDDLE) {
            viewX += mouseEvent.getX() - lastMousePosition[0];
            viewY += mouseEvent.getY() - lastMousePosition[1];
        }

        lastMousePosition[0] = mouseEvent.getX();
        lastMousePosition[1] = mouseEvent.getY();

        render();
    }

    private Node hitTest(double x, double y) {
        Node hitNode = null;

        for (Node node : tree.getNodes()) {
            int nodeHeight = 32 + (node.getInputs().size() + node.getOutputs().size()) * 24;

            if (x >= node.getX() && y >= node.getY() && x < node.getX() + 150 && y < node.getY() + nodeHeight) {
                hitNode = node;
            }
        }

        return hitNode;
    }

    private NodeSocket hitTestSocket(Node node, double x, double y) {
        if (node == null) {
            return null;
        }

        double yOffset = y - node.getY() - 30;
        int socketIndex = (int)(yOffset / 24);
        if (socketIndex < 0) {
            return null;
        }

        if (socketIndex < node.getInputs().size()) {
            return node.getInputs().get(socketIndex);
        } else if (socketIndex < node.getInputs().size() + node.getOutputs().size()) {
            return node.getOutputs().get(socketIndex - node.getInputs().size());
        }

        return null;
    }

    private double[] getSocketPosition(NodeSocket socket) {
        double[] pos = new double[2];

        Node node = socket.getParentNode();
        pos[0] = node.getX() + ((socket.getType() == NodeSocketType.INPUT) ? 5 : 145);
        pos[1] = node.getY() + 42;

        if (socket.getType() == NodeSocketType.INPUT) {
            pos[1] += node.getInputs().indexOf(socket) * 24;
        } else {
            pos[1] += node.getOutputs().indexOf(socket) * 24 + node.getInputs().size() * 24;
        }

        return pos;
    }

    private void renderNode(GraphicsContext ctx, Node node) {
        double xBase = node.getX();
        double yBase = node.getY();

        int socketCount = node.getInputs().size() + node.getOutputs().size();

        ctx.setFill((node == tree.getActiveNode()) ? Color.BLUE : Color.GRAY);
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
    }

    private void renderConnection(GraphicsContext ctx, NodeConnection conn) {
        NodeSocket from = conn.getOutputSocket();
        Node fromNode = from.getParentNode();
        int fromYOffset = 30 + 24 * (fromNode.getInputs().size() + fromNode.getOutputs().indexOf(from));

        NodeSocket to = conn.getInputSocket();
        Node toNode = to.getParentNode();
        int toYOffset = 30 + 24 * toNode.getInputs().indexOf(to);

        ctx.moveTo(fromNode.getX() + 149, fromNode.getY() + fromYOffset + 12);
        ctx.bezierCurveTo(
                fromNode.getX() + 180, fromNode.getY() + fromYOffset + 12,
                toNode.getX() - 30, toNode.getY() + toYOffset + 12,
                toNode.getX() + 1, toNode.getY() + toYOffset + 12
        );
    }

    public void render() {
        GraphicsContext ctx = getGraphicsContext2D();

        ctx.clearRect(0, 0, getWidth(), getHeight());

        ctx.save();
        ctx.translate(viewX, viewY);

        ctx.beginPath();
        ctx.setStroke(Color.GRAY);
        ctx.setLineWidth(2);
        for (NodeConnection conn : tree.getConnections()) {
            renderConnection(ctx, conn);
        }
        ctx.stroke();

        for (Node n : tree.getNodes()) {
            renderNode(ctx, n);
        }

        ctx.beginPath();
        ctx.setStroke(Color.RED);
        if (activeSocket != null) {
            double[] pos = getSocketPosition(activeSocket);
            ctx.moveTo(pos[0], pos[1]);
            ctx.lineTo(lastMousePosition[0] - viewX, lastMousePosition[1] - viewY);
        }
        ctx.stroke();

        ctx.restore();
    }
}

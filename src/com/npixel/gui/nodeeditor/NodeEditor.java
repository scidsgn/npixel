package com.npixel.gui.nodeeditor;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.tree.NodeConnection;
import com.npixel.base.tree.NodeTree;
import com.npixel.base.tree.NodeTreeEvent;
import com.npixel.gui.icons.Icons;
import com.npixel.gui.utils.TransparencyGrid;
import com.npixel.nodelibrary.NodeLibrary;
import com.npixel.nodelibrary.NodeLibraryCategory;
import com.npixel.nodelibrary.NodeLibraryNode;
import com.npixel.nodelibrary.source.SourceBitmapNode;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class NodeEditor extends Canvas {
    private final NodeTree tree;
    private NodeSocket activeSocket = null;

    private ContextMenu selectedNodeMenu;
    private ContextMenu addNodeMenu;

    private final Double[] lastMousePosition = {0.0, 0.0};
    double viewX = 0, viewY = 0;

    public NodeEditor(NodeTree tree) {
        this.tree = tree;

        prepareEvents();
        setupSelectedNodeMenu();
        setupAddNodeMenu();
    }

    private void prepareEvents() {
        this.setOnMousePressed(this::handleMouseDown);
        this.setOnMouseReleased(this::handleMouseRelease);
        this.setOnMouseDragged(this::handleDragging);
        this.setOnContextMenuRequested(this::handleContextMenu);

        widthProperty().addListener(event -> render());
        heightProperty().addListener(event -> render());

        tree.on(NodeTreeEvent.NODEAPPEARANCEUPDATED, node -> {
            render();
            return null;
        });
        tree.on(NodeTreeEvent.NODEUPDATED, node -> {
            render();
            return null;
        });
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

        MenuItem convertToBitmap = new MenuItem(
                "Convert to Pixel Layer", new ImageView(Icons.getIcon("sourcebitmap"))
        );
        ((ImageView)convertToBitmap.getGraphic()).setFitWidth(16);
        ((ImageView)convertToBitmap.getGraphic()).setFitHeight(16);
        convertToBitmap.setOnAction(event -> {
            Node bitmapNode = SourceBitmapNode.createFromNode(tree.getActiveNode());
            tree.setActiveNode(bitmapNode);

            render();
        });

        MenuItem exportPNG = new MenuItem("Export to PNG");
        exportPNG.setOnAction(event -> {
            Node node = tree.getActiveNode();

            if (node.getOutputs().size() > 0) {
                Bitmap bmp = (Bitmap)node.getOutputs().get(0).getValue();


                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save PNG");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("PNG", "*.png")
                );
                File selectedFile = fileChooser.showSaveDialog(null);

                if (selectedFile != null) {
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(bmp, null), "png", selectedFile);
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't export the image.");
                        alert.showAndWait();
                    }
                }
            }
        });

        selectedNodeMenu.getItems().addAll(
                deleteItem, disconnectItem,
                new SeparatorMenuItem(),
                convertToBitmap,
                exportPNG
        );
    }

    private MenuItem createNodeMenuItem(NodeLibraryNode node) {
        MenuItem nodeItem = new MenuItem(node.getName());
        nodeItem.setGraphic(node.getIconView());
        nodeItem.setOnAction(event -> addNode(node.create(tree)));

        return nodeItem;
    }

    private void setupAddNodeMenu() {
        addNodeMenu = new ContextMenu();

        for (NodeLibraryNode node : NodeLibrary.nodeLibrary.getSingleNodes()) {
            addNodeMenu.getItems().add(createNodeMenuItem(node));
        }

        addNodeMenu.getItems().add(new SeparatorMenuItem());

        for (NodeLibraryCategory category : NodeLibrary.nodeLibrary.getCategories()) {
            Menu categoryMenu = new Menu(category.getName());

            for (NodeLibraryNode node : category.getNodes()) {
                categoryMenu.getItems().add(createNodeMenuItem(node));
            }

            addNodeMenu.getItems().add(categoryMenu);

            if (category.isBeforeSeparator()) {
                addNodeMenu.getItems().add(new SeparatorMenuItem());
            }
        }
    }

    private void handleContextMenu(ContextMenuEvent contextMenuEvent) {
        if (tree.getActiveNode() != null) {
            selectedNodeMenu.show(this, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        } else {
            addNodeMenu.show(this, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        }
    }

    private void handleMouseDown(MouseEvent mouseEvent) {
        Node node = hitTest(mouseEvent.getX() - viewX, mouseEvent.getY() - viewY);

        selectedNodeMenu.hide();
        addNodeMenu.hide();

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

    private double calculateThumbnailOffset(Node node, boolean addSpace) {
        Bitmap thumbnail = node.getThumbnail();
        if (thumbnail != null) {
            return 142 * thumbnail.getHeight() / thumbnail.getWidth() + (addSpace ? 0.0 : 4.0);
        }

        return 0.0;
    }

    private Node hitTest(double x, double y) {
        Node hitNode = null;

        for (Node node : tree.getNodes()) {
            int nodeHeight = 32 + (node.getInputs().size() + node.getOutputs().size()) * 24 + (int)calculateThumbnailOffset(node, true);

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

        double yOffset = y - node.getY() - 30 - calculateThumbnailOffset(node, true);

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
        pos[1] = node.getY() + 42 + calculateThumbnailOffset(node, true);

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

        double thumbnailOffset = calculateThumbnailOffset(node, true);

        int socketCount = node.getInputs().size() + node.getOutputs().size();

        ctx.setFill(Color.color(0.9, 0.9, 0.9));
        ctx.fillRoundRect(xBase - 0.5, yBase + 0.5, 151, 32 + socketCount * 24 + thumbnailOffset, 8, 8);

        ctx.setFill(Color.WHITE);
        ctx.fillRoundRect(xBase - 0.5, yBase + 30.5, 151, 2 + socketCount * 24 + thumbnailOffset, 8, 8);

        ctx.setStroke((node == tree.getActiveNode()) ? Color.BLUE : Color.GRAY);
        ctx.setLineWidth(1);
        ctx.strokeRoundRect(xBase - 0.5, yBase - 0.5, 152, 34 + socketCount * 24 + thumbnailOffset, 8, 8);

        ctx.drawImage(node.getIcon(), xBase + 3, yBase + 3, 24, 24);

        ctx.setFill(Color.BLACK);
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setTextAlign(TextAlignment.LEFT);
        ctx.fillText(node.getName(), xBase + 32, yBase + 15);

        int i = 0;

        Bitmap thumbnail = node.getThumbnail();
        if (thumbnail != null) {
            ctx.setFill(Color.DARKGRAY);
            ctx.fillRect(xBase + 3, yBase + 31, 144, thumbnailOffset - 2);

            ctx.setFill(TransparencyGrid.grid.getPattern());
            ctx.fillRect(xBase + 4, yBase + 32, 142, thumbnailOffset - 4);

            ctx.setImageSmoothing(false);
            ctx.drawImage(thumbnail, xBase + 4, yBase + 32, 142, thumbnailOffset - 4);
            ctx.setImageSmoothing(true);
        }

        for (NodeSocket socket : node.getInputs()) {
            int yOffset = 30 + i * 24 + (int)thumbnailOffset;

            ctx.setFill(Color.BLACK);
            ctx.setTextAlign(TextAlignment.LEFT);
            ctx.fillText(socket.getName(), xBase + 16, yBase + yOffset + 12);

            ctx.setFill(Color.LIGHTGRAY);
            ctx.fillRect(xBase , yBase + yOffset + 7, 10, 10);

            ctx.setStroke((node == tree.getActiveNode()) ? Color.BLUE : Color.GRAY);
            ctx.setLineWidth(1);
            ctx.strokeRect(xBase - 0.5, yBase + yOffset + 6.5, 11, 11);

            i++;
        }
        for (NodeSocket socket : node.getOutputs()) {
            int yOffset = 30 + i * 24 + (int)thumbnailOffset;

            ctx.setFill(Color.BLACK);
            ctx.setTextAlign(TextAlignment.RIGHT);
            ctx.fillText(socket.getName(), xBase + 150 - 16, yBase + yOffset + 12);

            ctx.setFill(Color.LIGHTGRAY);
            ctx.fillRect(xBase + 141, yBase + yOffset + 7, 10, 10);

            ctx.setStroke((node == tree.getActiveNode()) ? Color.BLUE : Color.GRAY);
            ctx.setLineWidth(1);
            ctx.strokeRect(xBase + 140.5, yBase + yOffset + 6.5, 11, 11);

            i++;
        }

        if (node.requiresUpdate()) {
            ctx.setFill(Color.RED);
            ctx.fillOval(xBase + 146, yBase - 4, 8, 8);
        }
    }

    private void renderConnection(GraphicsContext ctx, NodeConnection conn) {
        NodeSocket from = conn.getOutputSocket();
        Node fromNode = from.getParentNode();
        int fromYOffset = 30 + 24 * (fromNode.getInputs().size() + fromNode.getOutputs().indexOf(from)) + (int)calculateThumbnailOffset(fromNode, true);

        NodeSocket to = conn.getInputSocket();
        Node toNode = to.getParentNode();
        int toYOffset = 30 + 24 * toNode.getInputs().indexOf(to) + (int)calculateThumbnailOffset(toNode, true);

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

        if (tree.isInvalid()) {
            ctx.setStroke(Color.RED);
            ctx.setLineWidth(8);
            ctx.strokeRect(0, 0, getWidth(), getHeight());
        }

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

    public void addNode(Node node) {
        if (node == null) {
            return;
        }

        node.setX((getWidth() - 150) / 2 - viewX);
        node.setY(getHeight() / 2 - 100 - viewY);
        tree.addNode(node);
        tree.setActiveNode(node);

        render();
    }
}

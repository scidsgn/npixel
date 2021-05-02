package com.npixel.gui;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.Document;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.IntProperty;
import com.npixel.base.properties.PropUtil;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.tool.ITool;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;
import com.npixel.nodelibrary.composite.CompositeCrossfadeNode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private static class TestBrush implements ITool {
        private final Bitmap bitmap;
        private final List<PropertyGroup> propertyGroups;

        public TestBrush(Bitmap bitmap) {
            this.bitmap = bitmap;

            propertyGroups = new ArrayList<>();
            propertyGroups.add(new PropertyGroup(
                    "brush", "Brush",
                    new IntProperty(this, "radius", "Radius", 1, 1, 10)
            ));
        }

        public String getName() {
            return "Brush";
        }

        public Image getIcon() {
            return Icons.getIcon("brush");
        }

        public boolean onMouseDragged(double endX, double endY, double movementX, double movementY) {
            double startX = endX - movementX;
            double startY = endY - movementY;

            double maxDelta = Math.ceil(Math.hypot(movementX, movementY));

            IntProperty radiusProp = (IntProperty)PropUtil.getProperty(this, "brush", "radius");
            int radius = (int)radiusProp.getValue();

            for (int i = 0; i < maxDelta; i++) {
                double x = 1 - ((double)i / maxDelta);

                for (int dx = -radius + 1; dx < radius; dx++) {
                    for (int dy = -radius + 1; dy < radius; dy++) {
                        if (dx != dy) {
                            continue;
                        }
                        bitmap.setPixel(
                                (int)(startX + movementX * x + dx), (int)(startY + movementY * x + dy),
                                new Color()
                        );
                    }
                }
            }

            return false;
        }

        public boolean onMousePressed(double x, double y) {
            return false;
        }

        public boolean onMouseReleased(double x, double y) {
            return true;
        }

        public void update() {}

        public List<PropertyGroup> getPropertyGroups() {
            return propertyGroups;
        }
    }

    private static class TestEraser implements ITool {
        private final Bitmap bitmap;
        private final List<PropertyGroup> propertyGroups;

        public TestEraser(Bitmap bitmap) {
            this.bitmap = bitmap;

            propertyGroups = new ArrayList<>();
            propertyGroups.add(new PropertyGroup(
                    "brush", "Brush",
                    new IntProperty(this, "radius", "Radius", 1, 1, 10)
            ));
        }

        public String getName() {
            return "Eraser";
        }

        public Image getIcon() {
            return Icons.getIcon("eraser");
        }

        public boolean onMouseDragged(double endX, double endY, double movementX, double movementY) {
            double startX = endX - movementX;
            double startY = endY - movementY;

            double maxDelta = Math.ceil(Math.hypot(movementX, movementY));

            IntProperty radiusProp = (IntProperty)PropUtil.getProperty(this, "brush", "radius");
            int radius = (int)radiusProp.getValue();

            for (int i = 0; i < maxDelta; i++) {
                double x = 1 - ((double)i / maxDelta);

                for (int dx = -radius + 1; dx < radius; dx++) {
                    for (int dy = -radius + 1; dy < radius; dy++) {
                        if (dx != dy) {
                            continue;
                        }
                        bitmap.setPixel(
                                (int)(startX + movementX * x + dx), (int)(startY + movementY * x + dy),
                                new Color(0, 0, 0, 0)
                        );
                    }
                }
            }

            return false;
        }

        public boolean onMousePressed(double x, double y) {
            return false;
        }

        public boolean onMouseReleased(double x, double y) {
            return true;
        }

        public void update() {}

        public List<PropertyGroup> getPropertyGroups() {
            return propertyGroups;
        }
    }

    private static class TestColorNode extends Node {
        TestColorNode(NodeTree tree, String name, Color color) {
            super(tree);

            typeString = "TestColor";
            this.name = name;

            Bitmap bitmap = new Bitmap(200, 200);
            outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", bitmap));

            tools.add(new TestBrush(bitmap));
            tools.add(new TestEraser(bitmap));
            setActiveTool(tools.get(0));

            fill(color);
        }

        private void fill(Color color) {
            Bitmap bmp = (Bitmap)getOutput("out").getValue();

            for (int x = 0; x < bmp.getWidth(); x++) {
                for (int y = 0; y < bmp.getHeight(); y++) {
                    bmp.setPixel(x, y, color);
                }
            }
        }

        @Override
        public Bitmap getThumbnail() {
            return (Bitmap)getOutput("out").getValue();
        }
    }

    private void createTestNodeTree(NodeTree tree) {
        TestColorNode blue = new TestColorNode(tree, "Blue", new Color(javafx.scene.paint.Color.BLUE));
        blue.setX(60);
        blue.setY(60);
        tree.addNode(blue);

        TestColorNode red = new TestColorNode(tree, "Red", new Color(javafx.scene.paint.Color.RED));
        red.setX(60);
        red.setY(200);
        tree.addNode(red);

        CompositeCrossfadeNode xfade = new CompositeCrossfadeNode(tree);
        xfade.setX(200);
        xfade.setY(40);
        tree.addNode(xfade);
    }

    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox();

        Document doc = new Document(300, 200);
        createTestNodeTree(doc.getTree());

        DocumentView docView = new DocumentView(doc);
        root.getChildren().add(docView);

        HBox.setHgrow(docView, Priority.ALWAYS);

        Scene scene = new Scene(root, 1500, 940);
        scene.getStylesheets().add(getClass().getResource("styles.css").toString());

        primaryStage.setTitle("NPIXEL");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

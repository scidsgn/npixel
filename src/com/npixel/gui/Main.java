package com.npixel.gui;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.Document;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.IntProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.properties.OptionProperty;
import com.npixel.base.tool.ITool;
import com.npixel.base.tree.NodeTree;
import com.npixel.nodelibrary.color.ColorCrossfadeNode;
import javafx.application.Application;
import javafx.scene.Scene;
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
        }

        public String getName() {
            return "test";
        }

        public boolean onMouseDragged(double endX, double endY, double movementX, double movementY) {
            double startX = endX - movementX;
            double startY = endY - movementY;

            double maxDelta = Math.max(Math.abs(movementX), Math.abs(movementY));

            for (int i = 0; i < maxDelta; i++) {
                double x = 1 - ((double)i / maxDelta);
                bitmap.setPixel((int)(startX + movementX * x), (int)(startY + movementY * x), new Color());
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

            ITool brushTool = new TestBrush(bitmap);
            tools.add(brushTool);
            setActiveTool(brushTool);

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
    private static class TestFadeNode extends Node {
        TestFadeNode(NodeTree tree, String name) {
            super(tree);

            typeString = "TestImg";
            this.name = name;

            propertyGroups.add(new PropertyGroup(
                    "blend", "Blend",
                    new IntProperty(this, "mix", "Mix", 0, 0, 10),
                    new OptionProperty(this, "test", "Test", 0, "A", "B", "C")
            ));

            inputs.add(new NodeSocket(this, "a", NodeSocketType.INPUT, "Bmp1", new Bitmap(200, 200)));
            inputs.add(new NodeSocket(this, "b", NodeSocketType.INPUT, "Bmp2", new Bitmap(200, 200)));
            outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(200, 200)));
        }

        @Override
        public void process() {
            Bitmap bmp1 = (Bitmap)getInputValue("a");
            Bitmap bmp2 = (Bitmap)getInputValue("b");

            Bitmap bmp = (Bitmap)getOutput("out").getValue();

            IntProperty mixProp = (IntProperty)getProperty("blend", "mix");
            double mixPropValue = (double)mixProp.getValue() / 10;

            System.out.println(((OptionProperty)getProperty("blend", "test")).getValue());

            bmp.scan((x, y, c) -> {
                double mix = (double)x / bmp.getWidth();
                return Color.mix(bmp1.getPixel(x, y), bmp2.getPixel(x, y), mix * mixPropValue);
            });

            super.process();
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

        TestFadeNode n5 = new TestFadeNode(tree, "Mixer");
        n5.setX(600);
        n5.setY(40);
        tree.addNode(n5);

        ColorCrossfadeNode xfade = new ColorCrossfadeNode(tree);
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

        primaryStage.setTitle("NPIXEL");
        primaryStage.setScene(new Scene(root, 1500, 940));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

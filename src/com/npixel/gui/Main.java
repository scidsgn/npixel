package com.npixel.gui;

import com.npixel.base.Bitmap;
import com.npixel.base.Document;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.tree.NodeTree;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private static class TestNode extends Node {
        TestNode(NodeTree tree, String name) {
            super(tree);

            typeString = "Test";
            this.name = name;

            inputs.add(new NodeSocket(this, "a", NodeSocketType.INPUT, "Number A", 2));
            inputs.add(new NodeSocket(this, "b", NodeSocketType.INPUT, "Number B", 2));
            outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", 0, "a"));
        }

        @Override
        public void process() {
            getOutput("out").setValue((Integer)getInputValue("a") + (Integer)getInputValue("b"));
            super.process();
        }
    }

    private static class TestColorNode extends Node {
        TestColorNode(NodeTree tree, String name, Color color) {
            super(tree);

            typeString = "TestColor";
            this.name = name;

            outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(200, 200)));

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
    }

    private static class TestFadeNode extends Node {
        TestFadeNode(NodeTree tree, String name) {
            super(tree);

            typeString = "TestImg";
            this.name = name;

            inputs.add(new NodeSocket(this, "a", NodeSocketType.INPUT, "Bmp1", new Bitmap(200, 200)));
            inputs.add(new NodeSocket(this, "b", NodeSocketType.INPUT, "Bmp2", new Bitmap(200, 200)));
            outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(200, 200)));
        }

        @Override
        public void process() {
            Bitmap bmp1 = (Bitmap)getInputValue("a");
            Bitmap bmp2 = (Bitmap)getInputValue("b");

            Bitmap bmp = (Bitmap)getOutput("out").getValue();

            for (int x = 0; x < bmp.getWidth(); x++) {
                for (int y = 0; y < bmp.getHeight(); y++) {
                    Color c = bmp1.getPixel(x, y);
                    if (x > y) {
                        c = bmp2.getPixel(x, y);
                    }

                    bmp.setPixel(x, y, c);
                }
            }

            super.process();
        }
    }

    private void createTestNodeTree(NodeTree tree) {
        TestColorNode blue = new TestColorNode(tree, "Blue", Color.BLUE);
        blue.setX(60);
        blue.setY(60);
        tree.addNode(blue);

        TestColorNode red = new TestColorNode(tree, "Red", Color.RED);
        red.setX(60);
        red.setY(200);
        tree.addNode(red);

        TestFadeNode n5 = new TestFadeNode(tree, "Mixer");
        n5.setX(600);
        n5.setY(40);
        tree.addNode(n5);
    }

    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox();

        Document doc = new Document(300, 200);
        createTestNodeTree(doc.getTree());

        DocumentView docView = new DocumentView(doc);
        root.getChildren().add(docView);

        HBox.setHgrow(docView, Priority.ALWAYS);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1140, 768));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

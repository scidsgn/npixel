package com.npixel.base.tree;

import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTreeTest {
    private static class TestNode extends Node {
        TestNode(NodeTree tree) {
            super(tree);

            inputs.add(new NodeSocket(this, "a", NodeSocketType.INPUT, "Number A", 2));
            inputs.add(new NodeSocket(this, "b", NodeSocketType.INPUT, "Number B", 2));
            outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", 0));
        }

        @Override
        public void process() {
            getOutput("out").setValue((Integer)getInputValue("a") + (Integer)getInputValue("b"));
        }
    }

    private NodeTree tree;
    private TestNode node1, node2, node3;

    @BeforeEach
    void setUp() {
        tree = new NodeTree();
        node1 = new TestNode(tree);
        node2 = new TestNode(tree);
        node3 = new TestNode(tree);

        tree.addNode(node1);
        tree.addNode(node2);
        tree.addNode(node3);

        node1.process();
        node2.process();
        node3.process();
    }

    @Test
    void addNode() {
        assertDoesNotThrow(() -> tree.connect(node1, "out", node2, "a"));
        assertDoesNotThrow(() -> tree.connect(node2, "out", node1, "a"));

        assertThrows(IllegalArgumentException.class, () -> tree.addNode(node1));
    }

    @Test
    void getConnectedOutput() {
        tree.connect(node1, "out", node2, "a");
        assertEquals(node1.getOutput("out"), tree.getConnectedOutput(node2.getInput("a")));
    }

    @Test
    void connect() {
        assertEquals(4, (Integer)node2.getOutput("out").getValue());

        tree.connect(node1, "out", node2, "a");
        node2.process();
        assertEquals(6, (Integer)node2.getOutput("out").getValue());
    }

    @Test
    void disconnectAll() {
        assertEquals(4, (Integer)node2.getOutput("out").getValue());
        assertEquals(4, (Integer)node3.getOutput("out").getValue());

        tree.connect(node1, "out", node2, "a");
        tree.connect(node2, "out", node3, "a");
        tree.connect(node2, "out", node3, "b");
        node2.process();
        node3.process();

        assertEquals(6, (Integer)node2.getOutput("out").getValue());
        assertEquals(12, (Integer)node3.getOutput("out").getValue());

        tree.disconnectAll(node2);
        node2.process();
        node3.process();

        assertEquals(4, (Integer)node2.getOutput("out").getValue());
        assertEquals(4, (Integer)node3.getOutput("out").getValue());
    }
}
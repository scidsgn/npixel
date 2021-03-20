package com.npixel.base.node;

import com.npixel.base.tree.NodeTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
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

    private TestNode node;

    @BeforeEach
    void setUp() {
        node = new TestNode(new NodeTree());
    }

    @Test
    void getOutput() {
        assertNotNull(node.getOutput("out"));
        assertEquals("Output", node.getOutput("out").getName());

        assertNull(node.getOutput("a"));
        assertNull(node.getOutput("b"));
    }

    @Test
    void getInput() {
        assertNotNull(node.getInput("a"));
        assertNotNull(node.getInput("b"));
        assertEquals("Number A", node.getInput("a").getName());
        assertEquals("Number B", node.getInput("b").getName());

        assertNull(node.getInput("c"));
        assertNull(node.getInput("out"));
    }

    @Test
    void process() {
        assertEquals(0, (Integer)node.getOutput("out").getValue());
        node.process();
        assertEquals(4, (Integer)node.getOutput("out").getValue());
    }
}
package com.npixel.nodelibrary.texture;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.OptionProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.properties.templates.SizePropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class TextureXORNode extends Node {
    public TextureXORNode(NodeTree tree) {
        super(tree);

        typeString = "TextureXOR";
        icon = Icons.getIcon("texturexor");

        propertyGroups.add(new SizePropertyGroup(this, "size", "Size", 32, 32, 1, 512));
        propertyGroups.add(new PropertyGroup("gate", "Operation",
                new OptionProperty(this, "gate", "Logic gate", 0, "XOR", "AND", "OR")
        ));

        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Texture", new Bitmap(1, 1)));
    }

    private double gate(int x, int y, int op) {
        if (op == 0) {
            return ((x % 256) ^ (y % 256)) / 255.0;
        } else if (op == 1) {
            return ((x % 256) & (y % 256)) / 255.0;
        } else {
            return ((x % 256) | (y % 256)) / 255.0;
        }
    }

    @Override
    public void process() {
        int w = SizePropertyGroup.getWidth(this, "size");
        int h = SizePropertyGroup.getHeight(this, "size");

        Bitmap outBitmap = new Bitmap(w, h);
        getOutput("out").setValue(outBitmap);

        int op = ((OptionProperty)getProperty("gate", "gate")).getValue();

        outBitmap.scan((x, y, c) -> {
            double xor = gate(x, y, op);
            return new Color(xor, xor, xor);
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

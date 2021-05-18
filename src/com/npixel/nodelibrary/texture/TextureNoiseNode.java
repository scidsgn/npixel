package com.npixel.nodelibrary.texture;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.DoubleProperty;
import com.npixel.base.properties.IntProperty;
import com.npixel.base.properties.OptionProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.properties.templates.SizePropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

import java.util.Random;

public class TextureNoiseNode extends Node {
    private final Random rng = new Random();
    private final Color gray = new Color(0.5, 0.5, 0.5);

    public TextureNoiseNode(NodeTree tree) {
        super(tree);

        typeString = "TextureNoise";
        icon = Icons.getIcon("none");

        propertyGroups.add(new SizePropertyGroup(this, "size", "Size", 32, 32, 1, 512));
        propertyGroups.add(new PropertyGroup(
                "noise", "Noise",
                new OptionProperty(this, "type", "Type", 0, "Color noise", "Grayscale noise"),
                new IntProperty(this, "seed", "Seed", 0, 0, 1023),
                new DoubleProperty(this, "strength", "Strength", 1, 0, 1)
        ));

        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Texture", new Bitmap(1, 1)));
    }

    @Override
    public void process() {
        int w = SizePropertyGroup.getWidth(this, "size");
        int h = SizePropertyGroup.getHeight(this, "size");

        int noiseType = ((OptionProperty)getProperty("noise", "type")).getValue();
        int seed = ((IntProperty)getProperty("noise", "seed")).getValue();
        double strength = ((DoubleProperty)getProperty("noise", "strength")).getValue();

        Bitmap outBitmap = new Bitmap(w, h);
        getOutput("out").setValue(outBitmap);

        rng.setSeed(seed);
        outBitmap.scan((x, y, c) -> {
            if (noiseType == 0) {
                return Color.mix(
                        gray,
                        new Color(rng.nextDouble(), rng.nextDouble(), rng.nextDouble()),
                        strength
                );
            } else {
                double noise = rng.nextDouble();
                return Color.mix(gray, new Color(noise, noise, noise), strength);
            }
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

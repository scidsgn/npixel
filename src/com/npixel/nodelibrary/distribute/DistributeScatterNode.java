package com.npixel.nodelibrary.distribute;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.templates.SizePropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;
import com.npixel.nodelibrary.distribute.properties.templates.DistributeBasicPropertyGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DistributeScatterNode extends Node {
    private final BitmapDistributor distributor = new BitmapDistributor();
    private final Random rng = new Random();

    public DistributeScatterNode(NodeTree tree) {
        super(tree);

        typeString = "DistributeScatter";
        icon = Icons.getIcon("none");

        propertyGroups.add(new SizePropertyGroup(this, "size", "Size", 100, 100, 1, 500));
        propertyGroups.add(new DistributeBasicPropertyGroup(this, "basic", "Basic settings", 10, 0));

        inputs.add(new NodeSocket(this, "obj1", NodeSocketType.INPUT, "Object 1", new Bitmap(1, 1)));
        inputs.add(new NodeSocket(this, "obj2", NodeSocketType.INPUT, "Object 2", new Bitmap(1, 1)));
        inputs.add(new NodeSocket(this, "obj3", NodeSocketType.INPUT, "Object 3", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(1, 1)));
    }

    private List<Bitmap> getObjects() {
        List<Bitmap> objects = new ArrayList<>();

        objects.add((Bitmap)getInputValue("obj1"));
        if (isInputConnected("obj2")) {
            objects.add((Bitmap)getInputValue("obj2"));
        }
        if (isInputConnected("obj3")) {
            objects.add((Bitmap)getInputValue("obj3"));
        }

        return objects;
    }

    @Override
    public void process() {
        int w = SizePropertyGroup.getWidth(this, "size");
        int h = SizePropertyGroup.getHeight(this, "size");

        Bitmap outBitmap = new Bitmap(w, h);
        getOutput("out").setValue(outBitmap);

        int n = DistributeBasicPropertyGroup.getN(this, "basic");
        int seed = DistributeBasicPropertyGroup.getSeed(this, "basic");

        distributor.clear();
        rng.setSeed(seed);

        for (int i = 0; i < n; i++) {
            distributor.add(rng.nextDouble(), rng.nextDouble());
        }

        distributor.distribute(outBitmap, getObjects());

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}

package com.npixel.base.properties;

import com.npixel.base.Vector;

public class NormalVectorProperty extends VectorProperty {
    public NormalVectorProperty(IUpdateable target, String id, String name, Vector value) {
        super(target, id, name, value.normalize());
    }

    @Override
    public void setValue(Vector value) {
        super.setValue(value.normalize());
    }
}

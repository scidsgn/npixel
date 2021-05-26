package com.npixel.base.properties;

import com.npixel.base.Document;

import java.util.List;

public interface IUpdateable {
    void update();
    Document getDocument();
    List<PropertyGroup> getPropertyGroups();
}

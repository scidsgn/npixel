package com.npixel.base.tool;

import com.npixel.base.properties.IUpdateable;
import javafx.scene.image.Image;

public interface ITool extends IUpdateable {
    String getName();
    Image getIcon();

    boolean onMouseDragged(double endX, double endY, double movementX, double movementY);
    boolean onMousePressed(double x, double y);
    boolean onMouseReleased(double x, double y);
}

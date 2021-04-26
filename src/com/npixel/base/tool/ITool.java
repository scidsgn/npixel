package com.npixel.base.tool;

public interface ITool {
    String getName();

    boolean onMouseDragged(double endX, double endY, double movementX, double movementY);
    boolean onMousePressed(double x, double y);
    boolean onMouseReleased(double x, double y);
}

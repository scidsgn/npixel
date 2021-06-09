package com.npixel.base;

import com.npixel.base.events.SimpleEventEmitter;
import javafx.scene.canvas.GraphicsContext;

public class ViewportCoordinates extends SimpleEventEmitter<DocumentEvent, ViewportCoordinates> {
    private double dx;
    private double dy;
    private double scaleFactor;

    public ViewportCoordinates(double dx, double dy, double scaleFactor) {
        super();

        this.dx = dx;
        this.dy = dy;
        this.scaleFactor = scaleFactor;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
        emit(DocumentEvent.NAMEUPDATED, this);
    }

    public void translate(double dx, double dy) {
        this.dx += dx;
        this.dy += dy;
    }

    public void translateInWorld(double dx, double dy) {
        this.dx += dx * scaleFactor;
        this.dy += dy * scaleFactor;
    }

    public void scaleTo(double newScale, double ox, double oy) {
        this.dx += scaleFactor * ox - newScale * ox;
        this.dy += scaleFactor * oy - newScale * oy;

        this.scaleFactor = newScale;
        emit(DocumentEvent.NAMEUPDATED, this);
    }

    public void scale(double factor, double ox, double oy) {
        scaleTo(factor * scaleFactor, ox, oy);
    }

    public void resetScale(double ox, double oy) {
        scale(1 / scaleFactor, ox, oy);
    }

    public double[] clientToWorld(double x, double y) {
        double[] out = new double[2];

        out[0] = (x - dx) / scaleFactor;
        out[1] = (y - dy) / scaleFactor;

        return out;
    }

    public double[] worldToClient(double x, double y) {
        double[] out = new double[2];

        out[0] = x * scaleFactor + dx;
        out[1] = y * scaleFactor + dy;

        return out;
    }

    public void transformContext(GraphicsContext ctx) {
        ctx.translate(dx, dy);
        ctx.scale(scaleFactor, scaleFactor);
    }

    public double deltaClientToWorld(double d) {
        return d / scaleFactor;
    }

    public double deltaWorldToClient(double d) {
        return d * scaleFactor;
    }
}

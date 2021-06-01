package com.npixel.nodelibrary.distribute;

import com.npixel.base.Vector;
import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BitmapDistributor {
    private final List<Vector> positions;
    private final List<Vector> convertedPositions;

    public BitmapDistributor() {
        positions = new ArrayList<>();
        convertedPositions = new ArrayList<>();
    }

    public void clear() {
        positions.clear();
    }
    
    public void add(double x, double y) {
        positions.add(new Vector(x, y, 0));
    }

    public void sort(Comparator<Vector> comparator) {
        positions.sort(comparator);
    }

    private Vector calculateBitmapPosition(Vector position, Bitmap target, Bitmap object) {
        double width = target.getWidth() - object.getWidth();
        double height = target.getHeight() - object.getHeight();

        return new Vector(position.getX() * width, position.getY() * height, 0);
    }

    private void convertPositions(Bitmap target, List<Bitmap> objects) {
        int i = 0;
        for (Vector position : positions) {
            Bitmap object = objects.get(i % objects.size());
            convertedPositions.add(calculateBitmapPosition(position, target, object));

            i++;
        }
    }

    private Color compositeObjects(int x, int y, List<Bitmap> objects) {
        Color out = new Color(1, 1, 1, 0);
        boolean one = false;
        int i = 0;

        for (Vector position : convertedPositions) {
            Bitmap object = objects.get(i % objects.size());
            i++;

            int objX = (int)(x - position.getX());
            int objY = (int)(y - position.getY());

            if (objX < 0 || objX >= object.getWidth() || objY < 0 || objY >= object.getHeight()) {
                continue;
            }

            out = Color.over(out, object.getPixel(objX, objY));
        }

        return out;
    }

    public void distribute(Bitmap target, List<Bitmap> objects) {
        convertedPositions.clear();
        convertPositions(target, objects);

        target.scan((x, y, c) -> compositeObjects(x, y, objects));
    }
}

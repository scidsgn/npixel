package com.npixel.base.tool.bitmap;

import com.npixel.base.Vector;
import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.gui.icons.Icons;
import javafx.scene.image.Image;

import java.util.LinkedList;

public class FloodFillTool extends BitmapBaseTool {
    public FloodFillTool(Node target, Bitmap bitmap) {
        super(target, bitmap);
    }

    private void floodFill(int x, int y, Color reference, Color target) {
        int width = (int)bitmap.getWidth();
        int height = (int)bitmap.getHeight();

        boolean[][] visited = new boolean[width][height];
        LinkedList<Vector> queue = new LinkedList<>();

        visited[x][y] = true;
        queue.add(new Vector(x, y));

        while (!queue.isEmpty()) {
            Vector position = queue.pop();
            int vx = (int)position.getX();
            int vy = (int)position.getY();

            bitmap.setPixel(vx, vy, target);

            if (vx - 1 >= 0 && bitmap.getPixel(vx - 1, vy).equals(reference) && !visited[vx - 1][vy]) {
                queue.push(new Vector(vx - 1, vy));
                visited[vx - 1][vy] = true;
            }
            if (vx + 1 < width && bitmap.getPixel(vx + 1, vy).equals(reference) && !visited[vx + 1][vy]) {
                queue.push(new Vector(vx + 1, vy));
                visited[vx + 1][vy] = true;
            }
            if (vy - 1 >= 0 && bitmap.getPixel(vx, vy - 1).equals(reference) && !visited[vx][vy - 1]) {
                queue.push(new Vector(vx, vy - 1));
                visited[vx][vy - 1] = true;
            }
            if (vy + 1 < height && bitmap.getPixel(vx, vy + 1).equals(reference) && !visited[vx][vy + 1]) {
                queue.push(new Vector(vx, vy + 1));
                visited[vx][vy + 1] = true;
            }
        }
    }

    @Override
    public String getName() {
        return "Fill";
    }

    @Override
    public Image getIcon() {
        return Icons.getIcon("floodfill");
    }

    @Override
    public boolean onMouseReleased(double x, double y) {
        if (x < 0 || x >= bitmap.getWidth() || y < 0 || y >= bitmap.getHeight()) {
            return false;
        }

        Color reference = bitmap.getPixel((int)x, (int)y);
        Color targetColor = target.getDocument().getForegroundColor();
        if (reference.equals(targetColor)) {
            return false;
        }

        floodFill((int)x, (int)y, reference, targetColor);

        return true;
    }
}

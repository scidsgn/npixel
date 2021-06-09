package com.npixel.base;

import com.npixel.base.bitmap.Color;
import com.npixel.base.events.SimpleEventEmitter;
import com.npixel.base.palette.NStopPalette;
import com.npixel.base.palette.Palette;
import com.npixel.base.tree.NodeTree;
import com.npixel.base.tree.NodeTreeEvent;
import com.npixel.nodelibrary.source.SourceBitmapNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Document extends SimpleEventEmitter<DocumentEvent, Document> {
    private String filePath;
    private NodeTree tree;
    private boolean modified = false;

    private Color foregroundColor = new Color();
    private Color backgroundColor = new Color(1, 1, 1);

    private final ObservableList<Palette> palettes;

    private final ViewportCoordinates coordinates;

    public Document(String filePath) {
        this.filePath = filePath;

        tree = new NodeTree(this);
        tree.on(NodeTreeEvent.NODEUPDATED, node -> {
            modified = true;
            emit(DocumentEvent.NAMEUPDATED, this);
            return null;
        });

        palettes = FXCollections.observableArrayList();
        coordinates = new ViewportCoordinates(32, 40, 1);
        coordinates.on(DocumentEvent.NAMEUPDATED, coordinates -> {
            emit(DocumentEvent.NAMEUPDATED, this);
            return null;
        });
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        modified = false;
        emit(DocumentEvent.NAMEUPDATED, this);
    }

    public String getShortName() {
        String name;
        if (filePath == null) {
            name = "Untitled";
        } else {
            Path path = Paths.get(filePath);
            name = path.getFileName().toString();
        }

        if (modified) {
            name = "*" + name;
        }

        return name;
    }

    public NodeTree getTree() {
        return tree;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        emit(DocumentEvent.COLORSUPDATED, this);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        emit(DocumentEvent.COLORSUPDATED, this);
    }

    public ObservableList<Palette> getPalettes() {
        return palettes;
    }

    public void initNewDocument() {
        generatePalettes();

        SourceBitmapNode bitmapNode = new SourceBitmapNode(tree);
        bitmapNode.setName("Pixel Layer");
        bitmapNode.setX(50);
        bitmapNode.setY(50);

        tree.addNode(bitmapNode);
    }

    private void generatePalettes() {
        palettes.add(new NStopPalette("Eight", 2));
        palettes.add(new NStopPalette("Web Safe", 6));
    }

    public ViewportCoordinates getCoordinates() {
        return coordinates;
    }

    public String getTabName() {
        return String.format("%s @ (%d%%)", getShortName(), (int)(coordinates.getScaleFactor() * 100));
    }
}

package com.npixel.base;

import com.npixel.base.bitmap.Color;
import com.npixel.base.events.SimpleEventEmitter;
import com.npixel.base.palette.NStopPalette;
import com.npixel.base.palette.Palette;
import com.npixel.base.tree.NodeTree;
import com.npixel.nodelibrary.source.SourceBitmapNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Document extends SimpleEventEmitter<DocumentEvent, Document> {
    private String fileName;
    private NodeTree tree;

    private Color foregroundColor = new Color();
    private Color backgroundColor = new Color(1, 1, 1);

    private final ObservableList<Palette> palettes;

    private final ViewportCoordinates coordinates;

    public Document(String fileName) {
        this.fileName = fileName;

        tree = new NodeTree(this);
        palettes = FXCollections.observableArrayList();
        coordinates = new ViewportCoordinates(32, 40, 1);
        coordinates.on(DocumentEvent.VIEWPORTSCALEUPDATED, coordinates -> {
            emit(DocumentEvent.VIEWPORTSCALEUPDATED, this);
            return null;
        });
    }

    public String getFileName() {
        return fileName;
    }

    public String getShortName() {
        if (fileName == null) {
            return "Untitled";
        }
        return fileName;
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

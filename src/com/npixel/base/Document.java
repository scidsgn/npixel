package com.npixel.base;

import com.npixel.base.bitmap.Color;
import com.npixel.base.events.SimpleEventEmitter;
import com.npixel.base.palette.NStopPalette;
import com.npixel.base.palette.Palette;
import com.npixel.base.tree.NodeTree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Document extends SimpleEventEmitter<DocumentEvent, Document> {
    private String fileName;
    private NodeTree tree;

    private Color foregroundColor = new Color();
    private Color backgroundColor = new Color(1, 1, 1);

    private final ObservableList<Palette> palettes;

    public Document(String fileName) {
        this.fileName = fileName;

        tree = new NodeTree(this);
        palettes = FXCollections.observableArrayList();
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
    }

    private void generatePalettes() {
        palettes.add(new NStopPalette("Eight", 2));
        palettes.add(new NStopPalette("Web Safe", 6));
    }
}

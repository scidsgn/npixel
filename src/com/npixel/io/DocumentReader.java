package com.npixel.io;

import com.npixel.base.Document;
import com.npixel.base.Vector;
import com.npixel.base.ViewportCoordinates;
import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.palette.NamedColor;
import com.npixel.base.palette.Palette;
import com.npixel.base.properties.*;
import com.npixel.base.tool.ITool;
import com.npixel.nodelibrary.NodeLibrary;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DocumentReader {
    private final DataInputStream stream;

    public DocumentReader(DataInputStream stream) {
        this.stream = stream;
    }

    private String readBytes(int n) throws IOException {
        byte[] bytes = stream.readNBytes(n);

        return new String(bytes, StandardCharsets.US_ASCII);
    }

    private String readIDString() throws IOException {
        int n = stream.readByte();
        return readBytes(n);
    }

    private String readFullString() throws IOException {
        int n = stream.readInt();
        char[] chars = new char[n];

        for (int i = 0; i < n; i++) {
            chars[i] = stream.readChar();
        }

        return new String(chars);
    }

    private Color readColor() throws IOException {
        return new Color(
                (double)stream.readUnsignedByte() / 255.0,
                (double)stream.readUnsignedByte() / 255.0,
                (double)stream.readUnsignedByte() / 255.0,
                (double)stream.readUnsignedByte() / 255.0
        );
    }

    private Vector readVector() throws IOException {
        return new Vector(
                stream.readDouble(),
                stream.readDouble(),
                stream.readDouble()
        );
    }

    private Bitmap readBitmap() throws IOException {
        int width = stream.readInt();
        int height = stream.readInt();
        Bitmap bitmap = new Bitmap(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, readColor());
            }
        }

        return bitmap;
    }

    private void readViewportCoordinates(Document doc) throws IOException {
        ViewportCoordinates coordinates = doc.getCoordinates();

        coordinates.setDx(stream.readDouble());
        coordinates.setDy(stream.readDouble());
        coordinates.setScaleFactor(stream.readDouble());
    }

    private void readNode(Document doc) throws IOException {
        String typeString = readIDString();
        Node node = NodeLibrary.nodeLibrary.createNodeFromId(doc.getTree(), typeString);

        node.setName(readFullString());
        node.setX(stream.readDouble());
        node.setY(stream.readDouble());

        doc.getTree().addNode(node);
    }

    private void readProperty(IUpdateable target) throws IOException {
        String groupId = readIDString();
        String propertyId = readIDString();

        IProperty property = PropUtil.getProperty(target, groupId, propertyId);

        if (property instanceof IntProperty) {
            ((IntProperty) property).setValue(stream.readInt());
        } else if (property instanceof DoubleProperty) {
            ((DoubleProperty) property).setValue(stream.readDouble());
        } else if (property instanceof BooleanProperty) {
            ((BooleanProperty) property).setValue(stream.readBoolean());
        } else if (property instanceof OptionProperty) {
            ((OptionProperty) property).setValue(stream.readByte());
        } else if (property instanceof ColorProperty) {
            ((ColorProperty) property).setValue(readColor());
        } else if (property instanceof VectorProperty) {
            ((VectorProperty) property).setValue(readVector());
        }
    }

    private void readNodeProperty(Document doc) throws IOException {
        int nodeIndex = stream.readInt();
        Node node = doc.getTree().getNodes().get(nodeIndex);

        readProperty(node);
    }

    private void readToolProperty(Document doc) throws IOException {
        int nodeIndex = stream.readInt();
        Node node = doc.getTree().getNodes().get(nodeIndex);

        int toolIndex = stream.readUnsignedByte();
        ITool tool = node.getTools().get(toolIndex);

        readProperty(tool);
    }

    private void readNodeBitmap(Document doc) throws IOException {
        int nodeIndex = stream.readInt();
        Node node = doc.getTree().getNodes().get(nodeIndex);

        int socketIndex = stream.readUnsignedByte();
        NodeSocket socket = node.getOutputs().get(socketIndex);

        Bitmap bitmap = readBitmap();
        socket.setValue(bitmap);
    }

    private void readNodeConnection(Document doc) throws IOException {
        int fromIndex = stream.readInt();
        int fromSocketIndex = stream.readUnsignedByte();
        Node from = doc.getTree().getNodes().get(fromIndex);
        NodeSocket fromSocket = from.getOutputs().get(fromSocketIndex);

        int toIndex = stream.readInt();
        int toSocketIndex = stream.readUnsignedByte();
        Node to = doc.getTree().getNodes().get(toIndex);
        NodeSocket toSocket = to.getInputs().get(toSocketIndex);

        doc.getTree().connect(fromSocket, toSocket);
    }

    private void readPalette(Document doc) throws IOException {
        String name = readFullString();
        Palette palette = new Palette(name);

        int colorCount = stream.readInt();

        for (int i = 0; i < colorCount; i++) {
            String colorName = readFullString();
            Color color = readColor();

            palette.getColors().add(new NamedColor(
                    colorName, color.getRed(), color.getGreen(), color.getBlue()
            ));
        }

        doc.getPalettes().add(palette);
    }

    public void readDocument(Document doc) throws IOException {
        String magic = readBytes(4);
        byte version = stream.readByte();

        if (!magic.equals("NPXL") || version != 0x10) {
            throw new IOException("Incorrect header");
        }

        while (true) {
            String tablePrefix = readBytes(4);

            if (tablePrefix.equals("VPRT")) {
                readViewportCoordinates(doc);
            } else if (tablePrefix.equals("NODE")) {
                readNode(doc);
            } else if (tablePrefix.equals("NPRP")) {
                readNodeProperty(doc);
            } else if (tablePrefix.equals("TPRP")) {
                readToolProperty(doc);
            } else if (tablePrefix.equals("NBMP")) {
                readNodeBitmap(doc);
            } else if (tablePrefix.equals("NCON")) {
                readNodeConnection(doc);
            } else if (tablePrefix.equals("PALT")) {
                readPalette(doc);
            } else if (tablePrefix.equals("FIN.")) {
                break;
            } else {
                throw new IOException("Unknown table ID");
            }
        }
    }
}

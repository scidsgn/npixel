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
import com.npixel.base.tree.NodeConnection;
import com.npixel.base.tree.NodeTree;

import java.io.DataOutputStream;
import java.io.IOException;

public class DocumentWriter {
    private final DataOutputStream stream;

    public DocumentWriter(DataOutputStream stream) {
        this.stream = stream;
    }

    private void writeIDString(String idString) throws IOException {
        stream.writeByte(idString.length());
        stream.writeBytes(idString);
    }

    private void writeFullString(String string) throws IOException {
        stream.writeInt(string.length());
        stream.writeChars(string);
    }

    private void writeColor(Color color) throws IOException {
        stream.writeByte(color.getByteRed());
        stream.writeByte(color.getByteGreen());
        stream.writeByte(color.getByteBlue());
        stream.writeByte(color.getByteAlpha());
    }

    private void writeVector(Vector vector) throws IOException {
        stream.writeDouble(vector.getX());
        stream.writeDouble(vector.getY());
        stream.writeDouble(vector.getZ());
    }

    private void writeBitmap(Bitmap bitmap) throws IOException {
        stream.writeInt((int)bitmap.getWidth());
        stream.writeInt((int)bitmap.getHeight());

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                writeColor(bitmap.getPixel(x, y));
            }
        }
    }

    private void writePalette(Palette palette) throws IOException {
        stream.writeBytes("PALT");
        writeFullString(palette.getName());
        stream.writeInt(palette.getColors().size());

        for (NamedColor color : palette.getColors()) {
            writeFullString(color.getName());
            writeColor(color);
        }
    }

    private void writeViewportCoordinates(ViewportCoordinates coordinates) throws IOException {
        stream.writeBytes("VPRT");
        stream.writeDouble(coordinates.getDx());
        stream.writeDouble(coordinates.getDy());
        stream.writeDouble(coordinates.getScaleFactor());
    }

    private void writeProperty(PropertyGroup propertyGroup, IProperty property) throws IOException {
        writeIDString(propertyGroup.getId());
        writeIDString(property.getId());

        if (property instanceof IntProperty) {
            stream.writeInt(((IntProperty) property).getValue());
        } else if (property instanceof DoubleProperty) {
            stream.writeDouble(((DoubleProperty) property).getValue());
        } else if (property instanceof BooleanProperty) {
            stream.writeBoolean(((BooleanProperty) property).getValue());
        } else if (property instanceof OptionProperty) {
            stream.writeByte(((OptionProperty) property).getValue());
        } else if (property instanceof ColorProperty) {
            writeColor(((ColorProperty) property).getValue());
        } else if (property instanceof VectorProperty) {
            writeVector(((VectorProperty) property).getValue());
        }
    }

    private void writeNodeProperty(int nodeIndex, PropertyGroup propertyGroup, IProperty property) throws IOException {
        stream.writeBytes("NPRP");
        stream.writeInt(nodeIndex);
        writeProperty(propertyGroup, property);
    }

    private void writeNodeToolProperty(int nodeIndex, int toolIndex, PropertyGroup propertyGroup, IProperty property) throws IOException {
        stream.writeBytes("TPRP");
        stream.writeInt(nodeIndex);
        stream.writeByte(toolIndex);
        writeProperty(propertyGroup, property);
    }

    private void writeNodeOutput(int nodeIndex, NodeSocket socket) throws IOException {
        Object value = socket.getValue();
        if (value instanceof Bitmap) {
            stream.writeBytes("NBMP");
            stream.writeInt(nodeIndex);
            stream.writeByte(socket.getParentNode().getOutputs().indexOf(socket));

            writeBitmap((Bitmap)value);
        }
    }

    private void writeNode(Node node) throws IOException {
        stream.writeBytes("NODE");
        writeIDString(node.getTypeString());
        writeFullString(node.getName());

        stream.writeDouble(node.getX());
        stream.writeDouble(node.getY());

        int nodeIndex = node.getTree().getNodes().indexOf(node);

        for (PropertyGroup propertyGroup : node.getPropertyGroups()) {
            for (IProperty property : propertyGroup.getProperties()) {
                writeNodeProperty(nodeIndex, propertyGroup, property);
            }
        }

        for (NodeSocket output : node.getOutputs()) {
            writeNodeOutput(nodeIndex, output);
        }

        for (ITool tool : node.getTools()) {
            int toolIndex = node.getTools().indexOf(tool);

            for (PropertyGroup propertyGroup : tool.getPropertyGroups()) {
                for (IProperty property : propertyGroup.getProperties()) {
                    writeNodeToolProperty(nodeIndex, toolIndex, propertyGroup, property);
                }
            }
        }
    }

    private void writeNodeConnection(NodeConnection connection) throws IOException {
        NodeSocket fromSocket = connection.getOutputSocket();
        Node fromNode = fromSocket.getParentNode();
        NodeSocket toSocket = connection.getInputSocket();
        Node toNode = toSocket.getParentNode();

        stream.writeBytes("NCON");
        stream.writeInt(fromNode.getTree().getNodes().indexOf(fromNode));
        stream.writeByte(fromNode.getOutputs().indexOf(fromSocket));

        stream.writeInt(toNode.getTree().getNodes().indexOf(toNode));
        stream.writeByte(toNode.getInputs().indexOf(toSocket));
    }

    private void writeNodeTree(NodeTree tree) throws IOException {
        for (Node node : tree.getNodes()) {
            writeNode(node);
        }
        for (NodeConnection connection : tree.getConnections()) {
            writeNodeConnection(connection);
        }
    }

    public void writeDocument(Document doc) throws IOException {
        stream.writeBytes("NPXL");
        stream.writeByte(0x10);

        writeViewportCoordinates(doc.getCoordinates());
        writeNodeTree(doc.getTree());

        for (Palette palette : doc.getPalettes()) {
            writePalette(palette);
        }

        stream.writeBytes("FIN.");
    }
}

package com.npixel.gui.icons;

import javafx.scene.image.Image;

public class Icons {
    public static Image getIcon(String iconName) {
        return new Image(Icons.class.getResource(iconName + ".png").toString());
    }
}

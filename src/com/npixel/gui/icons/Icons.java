package com.npixel.gui.icons;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Icons {
    public static Image getIcon(String iconName) {
        return new Image(Icons.class.getResource(iconName + ".png").toString());
    }

    public static ImageView getImageView(String iconName, int size) {
        ImageView imageView = new ImageView(getIcon(iconName));
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        return imageView;
    }
}

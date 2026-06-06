package io.github.maze.entities.projectile;

import io.github.maze.util.Angle;
import io.github.maze.util.Util;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class KunaiAssets {

    public Image getRotatedKunai(Angle angle) {
        Image image = Util.getScaledImage("/image/ninja/kunai/KUNAI.png");
        ImageView imageView = new ImageView(image);
        imageView.setRotate(angle.getDegrees());
        return image;
    }
}

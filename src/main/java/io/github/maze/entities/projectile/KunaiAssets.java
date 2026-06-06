package io.github.maze.entities.projectile;

import io.github.maze.util.Angle;
import io.github.maze.util.Util;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class KunaiAssets {

    private Image image = Util.getScaledImage("/image/ninja/kunai/KUNAI.png");

    public Image getRotatedKunai(Angle angle) {
        ImageView imageView = new ImageView(image);
        imageView.setRotate(angle.getDegrees() + 225);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        return imageView.snapshot(params, null);
    }
}

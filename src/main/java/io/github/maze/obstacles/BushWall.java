package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BushWall extends Obstacle {

    public static final BushWallAssets frames = new BushWallAssets();

    public Image cachedImage;

    public BushWall(GamePanel gp, double x, double y) {
        super(gp, x, y, 1, 1.5);
    }

    @Override public double getDepth() { return y + GamePanel.TILE_SIZE; }
    @Override public boolean getCollision() { return true; }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(frames.getTexture("single"), x, y);
    }

    @Override
    public void update() {
        // BushWall needs no updating
    }
}

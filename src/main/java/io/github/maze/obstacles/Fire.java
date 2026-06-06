package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import javafx.scene.canvas.GraphicsContext;

public class Fire extends Obstacle {

    final FireAssets fireAssets = new FireAssets();

    public Fire(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }

    @Override
    public void render(GraphicsContext g) {

    }

    @Override
    public void update() {

    }

    @Override public double getDepth() { return y + height; }
    @Override public boolean getCollision() { return false; }
}

package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import javafx.scene.canvas.GraphicsContext;

public class FireMonster extends Obstacle {

    private static final FireMonsterAssets fireMonsterAssets = new FireMonsterAssets();
    public FireMonster(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE * 2, GamePanel.TILE_SIZE);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(GraphicsContext gc) {

    }

    @Override public double getDepth() { return y + height; }
    @Override public boolean getCollision() { return true; }
}

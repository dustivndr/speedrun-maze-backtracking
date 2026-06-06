package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import io.github.maze.render.Camera;
import javafx.scene.canvas.GraphicsContext;

public class Hole extends Obstacle {

    private static HoleAssets holeAssets = new HoleAssets();

    boolean hasOpened = false;
    boolean collision = false;
    private Camera camera;

    int animationCounter = 0;

    public Hole(GamePanel gp, int col, int row) {
        super(gp,
                col * GamePanel.TILE_SIZE * GamePanel.SCALE,
                row * GamePanel.TILE_SIZE * GamePanel.SCALE,
                GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        camera = gp.camera;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!hasOpened) return;
        gc.drawImage(holeAssets.getImage(animationCounter), camera.getScreenX(x), camera.getScreenY(y));
    }

    @Override
    public void update() {

        int col = (int) (x / GamePanel.TILE_SIZE);
        int row = (int) (y / GamePanel.TILE_SIZE);
        if (gp.player.getTileX() == col &&
                gp.player.getTileY() == row) {
            hasOpened = true;
            collision = true;
        }
    }

    @Override public double getDepth() { return height + y; }
    @Override public boolean getCollision() { return collision; }
}

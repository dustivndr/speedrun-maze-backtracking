package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.canvas.GraphicsContext;

public class FlagLocked extends Obstacle {

    private static final FlagLockedAssets flagLockedAssets = new FlagLockedAssets();

    public FlagLocked(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }

    @Override
    public void render(GraphicsContext g) {
        double screenX = gp.camera.getScreenX(x);
        double screenY = gp.camera.getScreenY(y);

        g.drawImage(
                flagLockedAssets.getFrame(),
                screenX, screenY,
                GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }

    @Override
    public void update() {

        if (Util.checkAABB(gp.maze.player, this) && gp.maze.player.keyCount > 0) {
            gp.maze.player.flagCount++;
            gp.maze.player.keyCount--;
            removeObject = true;
        }
    }

    @Override
    public double getDepth() {
        return y + height;
    }

    @Override
    public boolean getCollision() {
        return false;
    }
}

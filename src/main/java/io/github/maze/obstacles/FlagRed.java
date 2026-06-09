package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.canvas.GraphicsContext;

public class FlagRed extends Obstacle {

    private static final FlagRedAssets flagRedAssets = new FlagRedAssets();

    public FlagRed(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }

    @Override
    public void render(GraphicsContext g) {
        double screenX = gp.camera.getScreenX(x);
        double screenY = gp.camera.getScreenY(y);

        g.drawImage(
                flagRedAssets.getFrame(),
                screenX, screenY,
                GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }

    @Override
    public void update() {

        if (Util.checkAABB(gp.maze.player, this)) {
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

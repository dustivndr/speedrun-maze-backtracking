package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;

import javafx.scene.canvas.GraphicsContext;

public class FlagGreen extends Obstacle {

    final FlagGreenAssets flagGreenAssets = new FlagGreenAssets();

    public FlagGreen(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }

    @Override
    public void render(GraphicsContext g) {
        double screenX = gp.camera.getScreenX(x);
        double screenY = gp.camera.getScreenY(y);

        g.drawImage(flagGreenAssets.getFlagGreenImage(), screenX, screenY, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }

    @Override
    public void update() {

        int col = (int) (x / GamePanel.TILE_SIZE);
        int row = (int) (y / GamePanel.TILE_SIZE);
        if (gp.maze.player.getTileX() == col &&
                gp.maze.player.getTileY() == row) {
            gp.maze.player.flagCount++;
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

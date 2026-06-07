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
        g.drawImage(flagGreenAssets.getFlagGreenImage(), x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
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
        return 0;
    }

    @Override
    public boolean getCollision() {
        return false;
    }

}

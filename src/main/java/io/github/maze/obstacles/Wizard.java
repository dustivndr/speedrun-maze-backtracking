package io.github.maze.obstacles;

import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.canvas.GraphicsContext;

public class Wizard extends Obstacle {

    final Player p;

    public Wizard(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);

        p = gp.player;
    }

    @Override
    public void render(GraphicsContext gc) {
//        gc.drawImage();
    }

    @Override
    public void update() {

        // check player
        if (isPlayerInCoordinateRange()) {

        }

        // update texture
    }

    public boolean isPlayerInCoordinateRange() {

        // wizard center pos
        double centerX = this.x + (this.width / 2.0);
        double centerY = this.y + (this.height / 2.0);

        // get radius
        double rangeRadius = (5 * GamePanel.TILE_SIZE) / 2.0;

        // boundaries
        double minX = centerX - rangeRadius;
        double maxX = centerX + rangeRadius;
        double minY = centerY - rangeRadius;
        double maxY = centerY + rangeRadius;

        // player center pos
        double playerCenterX = p.getX() + (p.getWidth() / 2.0);
        double playerCenterY = p.getY() + (p.getHeight() / 2.0);

        // check if player is inside bounds
        return playerCenterX >= minX && playerCenterX <= maxX &&
                playerCenterY >= minY && playerCenterY <= maxY;
    }

    @Override public double getDepth() { return y + height; }
    @Override public boolean getCollision() { return true; }
}

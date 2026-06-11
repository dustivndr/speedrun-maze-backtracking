package io.github.maze.obstacles;

import io.github.maze.audio.SoundManager;
import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import javafx.scene.canvas.GraphicsContext;

public class Elf extends Obstacle {

    private static final ElfAssets elfAssets = new ElfAssets();

    public Elf(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);

    }

    @Override
    public void render(GraphicsContext g) {

        double screenX = gp.camera.getScreenX(x);
        double screenY = gp.camera.getScreenY(y);

        g.drawImage(elfAssets.getFrame(), screenX, screenY);
    }

    @Override
    public void update() {
        if (isPlayerInCoordinateRange()) {
            SoundManager.ELF_VOICE.play();
            gp.maze.player.heal(gp.maze.player.MAX_HP);
        }
    }

    public boolean isPlayerInCoordinateRange() {

        // ninja center pos
        double centerX = this.x + (this.width / 2.0);
        double centerY = this.y + (this.height / 2.0);

        // get radius
        double rangeRadius = (3 * GamePanel.TILE_SIZE) / 2.0;

        // boundaries
        double minX = centerX - rangeRadius;
        double maxX = centerX + rangeRadius;
        double minY = centerY - rangeRadius;
        double maxY = centerY + rangeRadius;

        // player center pos
        double playerCenterX = gp.maze.player.getX() + (gp.maze.player.getWidth() / 2.0);
        double playerCenterY = gp.maze.player.getY() + (gp.maze.player.getHeight() / 2.0);

        // check if player is inside bounds
        return playerCenterX >= minX && playerCenterX <= maxX &&
                playerCenterY >= minY && playerCenterY <= maxY;

    }

    @Override
    public double getDepth() {
        return y + height;
    }

    @Override
    public boolean getCollision() {
        return true;
    }
}

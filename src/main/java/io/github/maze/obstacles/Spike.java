package io.github.maze.obstacles;

import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import javafx.scene.canvas.GraphicsContext;

public class Spike extends Obstacle implements GameObject {

    private static final SpikeAssets assets = new SpikeAssets();
    private boolean active;
    private boolean triggered;
    private Player player;

    public Spike(GamePanel gp, int x, int y) {
        super(gp, x, y,
              GamePanel.TILE_SIZE * GamePanel.SCALE,
              GamePanel.TILE_SIZE * GamePanel.SCALE);
        this.player = gp.player;
    }

    @Override
    public void update() {
        if (!triggered &&
                player.getTileX() == x &&
                player.getTileY() == y) {

            triggered = true;
            active = true;

            player.damage(10);
        }
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.drawImage(
                active
                        ? assets.getSpikeFrame()
                        : assets.getHiddenFrame(),
                x * 16,
                y * 16
        );
    }

    @Override
    public double getDepth() {
        return y + height;
    }
}

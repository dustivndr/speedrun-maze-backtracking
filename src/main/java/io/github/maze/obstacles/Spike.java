package io.github.maze.obstacles;

import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Spike extends Obstacle implements GameObject {

    private static final SpikeAssets assets = new SpikeAssets();
    private boolean active;
    private boolean triggered;
    private Player player;

    public Spike(GamePanel gp, double x, double y) {
        super(gp, x, y,
              GamePanel.TILE_SIZE * GamePanel.SCALE,
              GamePanel.TILE_SIZE * GamePanel.SCALE);
        this.player = gp.player;
    }

    @Override
    public void update() {
        if (player.getTileX() == x / GamePanel.TILE_SIZE &&
                player.getTileY() == y / GamePanel.TILE_SIZE) {

            if (triggered) {
                return;
            }

            triggered = true;
            active = true;

            player.damage(10);
        } else {
            active = false;
            triggered = false;
        }
    }

    @Override
    public void render(GraphicsContext gc) {

        Image texture;

        if (active) {
            texture = assets.getSpikeFrame();
        } else {
            texture = assets.getHiddenFrame();
        }

        gc.drawImage(texture, gp.camera.getScreenX(x), gp.camera.getScreenY(y));
    }

    @Override public double getDepth() { return y + height; }
    @Override public boolean getCollision() { return false; }
}

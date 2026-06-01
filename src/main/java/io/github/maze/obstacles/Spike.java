package io.github.maze.obstacles;

import io.github.maze.entities.Player;
import io.github.maze.maze.GameObject;
import javafx.scene.canvas.GraphicsContext;

public class Spike extends Obstacle implements GameObject {

    private final SpikeAssets assets;
    private boolean active;
    private boolean triggered;

    public Spike(int x, int y, SpikeAssets assets) {
        super(x, y);
        this.assets = assets;
    }

    @Override
    public void update(Player player) {
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
    public void draw(GraphicsContext g) {

    }

    @Override
    public void update() {

    }

    @Override
    public double getDepth() {
        return 0;
    }
}

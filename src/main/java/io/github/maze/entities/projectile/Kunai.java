package io.github.maze.entities.projectile;

import io.github.maze.entities.Projectile;
import io.github.maze.game.GamePanel;
import io.github.maze.util.Angle;
import javafx.scene.canvas.GraphicsContext;

public class Kunai extends Projectile {

    Angle angle;

    public Kunai(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE * 3 / 4.0, GamePanel.TILE_SIZE * 3 / 4.0);

        angle = new Angle(Angle.between(x, y, gp.player.getX(), gp.player.getY()));
    }

    @Override
    protected void onUpdate() {

    }

    @Override
    protected boolean checkDelete() {
        return true;
    }

    @Override
    public void render(GraphicsContext g) {

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

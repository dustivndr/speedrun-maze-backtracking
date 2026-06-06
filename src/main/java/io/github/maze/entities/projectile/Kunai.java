package io.github.maze.entities.projectile;

import io.github.maze.entities.Projectile;
import io.github.maze.game.GamePanel;
import io.github.maze.util.Angle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class Kunai extends Projectile {

    private final Angle angle;
    private Image cachedImage;
    private double damage = 10;
    private double speed = 20;

    private double travelledDistance = 0;
    private final double MAX_TRAVELLED_DISTANCE;

    public Kunai(GamePanel gp, double x, double y) {
        super(gp, x, y, 4 * GamePanel.SCALE, 4 * GamePanel.SCALE);

        MAX_TRAVELLED_DISTANCE = GamePanel.TILE_SIZE * 2;

        angle = new Angle(Angle.between(x, y, gp.player.getX(), gp.player.getY()));
        cachedImage = (new KunaiAssets()).getRotatedKunai(angle);
    }

    @Override
    protected void onUpdate() {
        travelledDistance += speed;
        x += angle.cos() * speed;
        y += angle.sin() * speed;
    }

    @Override
    protected boolean checkDelete() {
        if (travelledDistance >= MAX_TRAVELLED_DISTANCE) {
            return true;
        }

        int col = (int) (x / GamePanel.TILE_SIZE);
        int row = (int) (y / GamePanel.TILE_SIZE);
        if (col == gp.player.getTileX() && row == gp.player.getTileY()) {
            return true;
        }

        return false;
    }

    @Override
    public void render(GraphicsContext g) {

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

package io.github.maze.obstacles;

import io.github.maze.entities.Entity;
import io.github.maze.entities.Player;
import io.github.maze.game.Game;
import io.github.maze.game.GamePanel;
import io.github.maze.util.Angle;
import javafx.scene.canvas.GraphicsContext;

public class Ninja extends Obstacle {

    NinjaAssets ninjaAssets = new NinjaAssets();
    Player p;

    Angle angleToPlayer;

    String spriteDirection = "down";

    final int damage = 5;

    private boolean playerWasInside = false;
    private double playerStartX = 0;
    private double playerStartY = 0;

    public Ninja(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        p = gp.player;
        angleToPlayer = new Angle(Angle.between(x, y, gp.player.getX(), gp.player.getY()));
    }

    @Override
    public void update() {

        Player p = gp.player;
        angleToPlayer.lookAt(x, y, p.getX(), p.getY());

        boolean isPlayerInside = p.getX() >= x - GamePanel.TILE_SIZE &&
                p.getX() <= x + GamePanel.TILE_SIZE &&
                p.getY() >= y - GamePanel.TILE_SIZE &&
                p.getY() <= y + GamePanel.TILE_SIZE;

        if (isPlayerInside) {
            if (!playerWasInside) {
                p.damage(damage);

                playerStartX = p.getX();
                playerStartY = p.getY();
                playerWasInside = true;
            }

            else {
                double distanceX = Math.abs(p.getX() - playerStartX);
                double distanceY = Math.abs(p.getY() - playerStartY);

                if (distanceX >= GamePanel.TILE_SIZE || distanceY >= GamePanel.TILE_SIZE) {
                    p.damage(damage);

                    playerStartX = p.getX();
                    playerStartY = p.getY();
                }
            }
        } else {
            playerWasInside = false;
        }

        angleToPlayer.lookAt(x, y, p.getX(), p.getY());
        if (angleToPlayer.getRadians() >= Math.PI / 4 && angleToPlayer.getRadians() < Math.PI / 4 * 3) {
            spriteDirection = "down";
        } else if (angleToPlayer.getRadians() >= Math.PI / 4 * 3 && angleToPlayer.getRadians() < Math.PI / 4 * 5) {
            spriteDirection = "left";
        } else if (angleToPlayer.getRadians() >= Math.PI / 4 * 5 && angleToPlayer.getRadians() < Math.PI / 2 * 7) {
            spriteDirection = "up";
        } else {
            spriteDirection = "right";
        }
    }

    @Override
    public void render(GraphicsContext g) {
        g.drawImage(
                ninjaAssets.getFrame(spriteDirection),
                gp.camera.getScreenX(x),
                gp.camera.getScreenY(y));
    }

    @Override public double getDepth() { return y + height; }
    @Override public boolean getCollision() { return true; }
}

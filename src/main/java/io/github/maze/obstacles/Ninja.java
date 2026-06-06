package io.github.maze.obstacles;

import io.github.maze.entities.Entity;
import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.util.Angle;
import javafx.scene.canvas.GraphicsContext;

public class Ninja extends Obstacle {

    NinjaAssets ninjaAssets = new NinjaAssets();
    Player p;

    Angle angleToPlayer;

    String spriteDirection = "down";

    final int damage = 5;

    int playerInsideWalkCounter = 0;

    public Ninja(GamePanel gp, double x, double y, double width, double height) {
        super(gp, x, y, width, height);
        p = gp.player;
        angleToPlayer = new Angle(Angle.between(x, y, gp.player.getX(), gp.player.getY()));
    }

    @Override
    public void update() {

        Player p = gp.player;
        angleToPlayer.lookAt(x, y, p.getX(), p.getY());

        if (
                p.getX() >= x - GamePanel.TILE_SIZE &&
                p.getX() <= x + GamePanel.TILE_SIZE &&
                p.getY() >= y - GamePanel.TILE_SIZE &&
                p.getY() <= y + GamePanel.TILE_SIZE
        ) {
            if (playerInsideWalkCounter % 2 == 0) {
                p.damage(damage);
            }

            playerInsideWalkCounter++;

        } else {
            playerInsideWalkCounter = 0;
        }

        if (angleToPlayer.getRadians() >= 0 && angleToPlayer.getRadians() < Math.PI / 2) {
            spriteDirection = "up";
        }
        else if (angleToPlayer.getRadians() >= Math.PI / 2 && angleToPlayer.getRadians() < Math.PI) {
            spriteDirection = "right";
        }
        else if (angleToPlayer.getRadians() >= Math.PI && angleToPlayer.getRadians() < Math.PI / 2 * 3) {
            spriteDirection = "down";
        }
        if (angleToPlayer.getRadians() >= Math.PI / 2 * 3 && angleToPlayer.getRadians() < Math.PI * 2) {
            spriteDirection = "left";
        }
    }

    @Override
    public void render(GraphicsContext g) {
        g.drawImage(ninjaAssets.getFrame(spriteDirection), x, y);
    }

    @Override public double getDepth() { return y + height; }
    @Override public boolean getCollision() { return true; }
}

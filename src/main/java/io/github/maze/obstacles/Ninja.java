package io.github.maze.obstacles;

import io.github.maze.entities.Player;
import io.github.maze.entities.projectile.Kunai;
import io.github.maze.game.GamePanel;
import io.github.maze.util.Angle;
import javafx.scene.canvas.GraphicsContext;

public class Ninja extends Obstacle {

    private static final NinjaAssets ninjaAssets = new NinjaAssets();
    Player p;

    Angle angleToPlayer;

    String spriteDirection = "down";

    final int damage = 5;

    private boolean playerWasInside = false;
    private int walkCountOnEntry = -1;
    private int walkCountAtLastAttack = -1;

    public Ninja(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        p = gp.maze.player;
        angleToPlayer = new Angle(Angle.between(x, y, gp.maze.player.getX(), gp.maze.player.getY()));
    }

    @Override
    public void update() {

        Player p = gp.maze.player;
        angleToPlayer.lookAt(x, y, p.getX(), p.getY());

        boolean isPlayerInside = isPlayerInCoordinateRange();

        if (isPlayerInside) {

            if (!playerWasInside) {
                walkCountOnEntry = p.getWalkCount();
                walkCountAtLastAttack = p.getWalkCount();

                attack();
                playerWasInside = true;
            }

            else {
                int currentWalkCount = p.getWalkCount();
                int tilesWalkedSinceEntry = currentWalkCount - walkCountOnEntry;

                if (tilesWalkedSinceEntry % 2 == 0 && currentWalkCount != walkCountAtLastAttack) {
                    attack();
                    walkCountAtLastAttack = currentWalkCount;
                }
            }
        } else {

            playerWasInside = false;
            walkCountOnEntry = -1;
            walkCountAtLastAttack = -1;
        }

        // update texture
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
        double playerCenterX = p.getX() + (p.getWidth() / 2.0);
        double playerCenterY = p.getY() + (p.getHeight() / 2.0);

        // check if player is inside bounds
        return playerCenterX >= minX && playerCenterX <= maxX &&
                playerCenterY >= minY && playerCenterY <= maxY;

    }

    private void attack() {
        gp.maze.objectList.add(new Kunai(gp, x, y));
        p.damage(damage);
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

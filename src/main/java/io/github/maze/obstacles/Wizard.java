package io.github.maze.obstacles;

import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.util.Angle;
import io.github.maze.util.Util;
import javafx.scene.canvas.GraphicsContext;

public class Wizard extends Obstacle {

    final WizardAssets wizardAssets;

    final Player p;

    private boolean isAttacking = false;

    Angle angleToPlayer;
    String spriteDirection = "down";

    int attackAnimationCounter = -1;
    long lastTime = 0;
    final long animationTimerMs = 350;
    final long animationLength = animationTimerMs * 7;
    long currAnimationLength = 0;

    public Wizard(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);

        wizardAssets = new WizardAssets();
        angleToPlayer = new Angle(Angle.between(x, y, gp.player.getX(), gp.player.getY()));
        p = gp.player;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isAttacking) {
            gc.drawImage(wizardAssets.getDirection(spriteDirection), x, y);
        } else {
            gc.drawImage(wizardAssets.getAttackAnimation(attackAnimationCounter), x, y);
        }
    }

    @Override
    public void update() {

        boolean playerInRange = isPlayerInCoordinateRange();

        // check state change
        if (playerInRange && !isAttacking) {
            isAttacking = true;
            currAnimationLength = 0;
            attackAnimationCounter = 0;
            lastTime = System.currentTimeMillis();
        }

        // attacking
        if (isAttacking) {
            long curr = System.currentTimeMillis();
            long elapsedTime = curr - lastTime;
            lastTime = curr;

            currAnimationLength += elapsedTime;

            int calculatedFrame = (int) (currAnimationLength / animationTimerMs);

            if (calculatedFrame < wizardAssets.attackSize()) {
                attackAnimationCounter = calculatedFrame;
            }

            if (currAnimationLength >= animationLength) {

                if (isPlayerInCoordinateRange()) {
                    p.damage(20);
                }

                currAnimationLength = 0;
                attackAnimationCounter = -1;

                // If player left the 5x5 area during the animation, drop aggro entirely
                if (!playerInRange) {
                    isAttacking = false;
                }
            }
        }

        // idle
        else {
            angleToPlayer.lookAt(x, y, p.getX(), p.getY());
            double rads = angleToPlayer.getRadians();

            // Standardized directional check mapping (handling normalization boundaries)
            if (rads >= Math.PI / 4 && rads < Math.PI / 4 * 3) {
                spriteDirection = "down";
            } else if (rads >= Math.PI / 4 * 3 && rads < Math.PI / 4 * 5) {
                spriteDirection = "left";
            } else if (rads >= Math.PI / 4 * 5 && rads < Math.PI / 4 * 7) { // Fixed math: PI/2 * 7 was out of bounds
                spriteDirection = "up";
            } else {
                spriteDirection = "right";
            }
        }
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

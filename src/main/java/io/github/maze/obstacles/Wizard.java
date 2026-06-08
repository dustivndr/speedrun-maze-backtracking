package io.github.maze.obstacles;

import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import io.github.maze.util.Angle;
import javafx.scene.canvas.GraphicsContext;

public class Wizard extends Obstacle {

    private static final WizardAssets wizardAssets = new WizardAssets();

    final Player p;

    private boolean isAttacking = false;

    private int walkCountOnEntry = -1;
    private int walkCountAtLastAttack = -1;
    private boolean playerWasInRangeLastFrame = false;

    Angle angleToPlayer;
    String spriteDirection = "down";

    int attackAnimationCounter = -1;
    long lastTime = 0;
    final long animationTimerMs = 350;
    final long animationLength = animationTimerMs * 7;
    long currAnimationLength = 0;

    public Wizard(GamePanel gp, double x, double y) {
        super(gp, x, y,
                GamePanel.TILE_SIZE,
                GamePanel.TILE_SIZE + 2 * GamePanel.SCALE);

        angleToPlayer = new Angle(Angle.between(x, y, gp.maze.player.getX(), gp.maze.player.getY()));
        p = gp.maze.player;
    }

    @Override
    public void render(GraphicsContext gc) {
        double screenX = gp.camera.getScreenX(x);
        double screenY = gp.camera.getScreenY(y - 2 * GamePanel.SCALE);

        if (!isAttacking) {
            gc.drawImage(wizardAssets.getDirection(spriteDirection), screenX, screenY);
        } else {
            gc.drawImage(wizardAssets.getAttackAnimation(attackAnimationCounter), screenX, screenY);
        }
    }

    @Override
    public void update() {

        boolean playerInRange = isPlayerInCoordinateRange();

        // 1. Handle initial entry into range
        if (playerInRange && !playerWasInRangeLastFrame) {
            walkCountOnEntry = p.getWalkCount();
            walkCountAtLastAttack = -1; // Reset history on fresh entry
        }
        playerWasInRangeLastFrame = playerInRange; // Update tracking state

        // check state change
        if (playerInRange && !isAttacking) {

            // Calculate how many tiles the player has walked *since entering range*
            int tilesWalkedSinceEntry = p.getWalkCount() - walkCountOnEntry;

            // Trigger attack on initial entry (0 tiles) and every 4 tiles thereafter (2, 4, 6...)
            if (tilesWalkedSinceEntry % 4 == 0 && p.getWalkCount() != walkCountAtLastAttack) {
                isAttacking = true;
                walkCountAtLastAttack = p.getWalkCount();
                currAnimationLength = 0;
                attackAnimationCounter = 0;
                lastTime = System.currentTimeMillis();
            }
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

                double playerCenterX = p.getX() + (p.getWidth() / 2.0);
                double playerBottomY = p.getY() + p.getHeight();

                gp.maze.addThunder(playerCenterX, playerBottomY);

                p.damage(10);

                currAnimationLength = 0;
                attackAnimationCounter = -1;

                isAttacking = false;
            }
        }

        // idle
        else {
            angleToPlayer.lookAt(x, y, p.getX(), p.getY());
            double rads = angleToPlayer.getRadians();

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

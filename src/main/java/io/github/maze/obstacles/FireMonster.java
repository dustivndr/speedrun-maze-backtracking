package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import javafx.scene.canvas.GraphicsContext;

public class FireMonster extends Obstacle {

    private static final FireMonsterAssets fireMonsterAssets = new FireMonsterAssets();

    private int idleNum = 0;

    private boolean isAttacking = false;

    long lastTime = 0;
    long idleTimerMs = 150;

    int attackAnimationCounter = -1;
    final long animationTimerMs = 150;
    final long animationLength = animationTimerMs * 4;
    long currAnimationLength = 0;

    public FireMonster(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE * 2, GamePanel.TILE_SIZE);
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

            if (calculatedFrame < fireMonsterAssets.attackSize()) {
                attackAnimationCounter = calculatedFrame;
            }

            if (currAnimationLength >= animationLength) {

                double playerCenterX = gp.maze.player.getX() + (gp.maze.player.getWidth() / 2.0);
                double playerBottomY = gp.maze.player.getY() + gp.maze.player.getHeight();

//                gp.maze.addThunder(playerCenterX, playerBottomY);

                gp.maze.player.damage(20);
                System.out.println("fire monster attacked player");

                currAnimationLength = 0;
                attackAnimationCounter = -1;

                // if player left the 5x5 area during the animation, drop aggro
                if (!playerInRange) {
                    isAttacking = false;
                }
            }
        }

        // idle
        else {

            long curr = System.currentTimeMillis();
            long elapsedTime = curr - lastTime;

            if (elapsedTime > idleTimerMs) {

                lastTime = System.currentTimeMillis();

                idleNum = (idleNum + 1) % fireMonsterAssets.idleSize();
            }

        }

    }

    public boolean isPlayerInCoordinateRange() {

        // fire monster center pos
        double centerX = this.x + (this.width / 2.0);
        double centerY = this.y + (this.height / 2.0);

        // get radius
        double rangeWidth = (6 * GamePanel.TILE_SIZE) / 2.0;
        double rangeHeight = (5 * GamePanel.TILE_SIZE) / 2.0;

        // boundaries
        double minX = centerX - rangeWidth;
        double maxX = centerX + rangeWidth;
        double minY = centerY - rangeHeight;
        double maxY = centerY + rangeHeight;

        // player center pos
        double playerCenterX = gp.maze.player.getX() + (gp.maze.player.getWidth() / 2.0);
        double playerCenterY = gp.maze.player.getY() + (gp.maze.player.getHeight() / 2.0);

        // check if player is inside bounds
        return playerCenterX >= minX && playerCenterX <= maxX &&
                playerCenterY >= minY && playerCenterY <= maxY;

    }

    @Override
    public void render(GraphicsContext gc) {

        double screenX = gp.camera.getScreenX(x);
        double screenY = gp.camera.getScreenY(y - GamePanel.TILE_SIZE);

        if (isAttacking) {
            gc.drawImage(fireMonsterAssets.getAttack(attackAnimationCounter), screenX, screenY);
        }
        else {
            gc.drawImage(fireMonsterAssets.getIdle(idleNum), screenX, screenY);
        }

    }

    @Override public double getDepth() { return y + height; }
    @Override public boolean getCollision() { return true; }
}

package io.github.maze.game;

import io.github.maze.entities.Player;

public class AutoPlayer {

    private final Player player;
    private final String path;

    private int currentStep = 0;

    // logical tile position (IMPORTANT FIX)
    private int logicalX;
    private int logicalY;

    private int targetX;
    private int targetY;

    private int currentDx = 0;
    private int currentDy = 0;

    private boolean waitingMove = false;

    public AutoPlayer(Player player, String path) {
        this.player = player;
        this.path = path;

        this.player.setAutoMode(true);

        // FIX: snapshot initial tile position (NOT pixel)
        this.logicalX = player.getTileX();
        this.logicalY = player.getTileY();
    }

    public void update() {

        if (path == null || path.isEmpty()) return;

        // =========================
        // WAIT UNTIL ARRIVE TARGET
        // =========================
        if (waitingMove) {

            double targetPixelX = targetX * GamePanel.TILE_SIZE;
            double targetPixelY = targetY * GamePanel.TILE_SIZE;

            boolean reached =
                    (currentDx == 1 && player.getX() >= targetPixelX) ||
                    (currentDx == -1 && player.getX() <= targetPixelX) ||
                    (currentDy == 1 && player.getY() >= targetPixelY) ||
                    (currentDy == -1 && player.getY() <= targetPixelY);

            if (reached) {

                // snap EXACT
                player.setX(targetPixelX);
                player.setY(targetPixelY);

                // FIX: update logical position (IMPORTANT)
                logicalX = targetX;
                logicalY = targetY;

                waitingMove = false;
                player.setAutoDirection(0, 0);

                currentStep++;
                return;
            }

            // FIX: safer stuck detection (epsilon instead of exact double compare)
            boolean stuck =
                    Math.abs(player.getX() - player.lastX) < 0.0001 &&
                    Math.abs(player.getY() - player.lastY) < 0.0001;

            if (stuck) {
                player.setAutoDirection(0, 0);
                player.setAutoMode(false);
            }

            return;
        }

        // =========================
        // FINISH
        // =========================
        if (currentStep >= path.length()) {
            player.setAutoDirection(0, 0);
            player.setAutoMode(false);
            return;
        }

        // =========================
        // NEXT STEP
        // =========================
        char move = path.charAt(currentStep);

        // FIX CRITICAL BUG (reset BOTH directions)
        currentDx = 0;
        currentDy = 0;

        switch (move) {
            case 'U' -> currentDy = -1;
            case 'D' -> currentDy = 1;
            case 'L' -> currentDx = -1;
            case 'R' -> currentDx = 1;
        }

        // FIX: use LOGICAL position, NOT player pixel-derived tile
        targetX = logicalX + currentDx;
        targetY = logicalY + currentDy;

        player.setAutoDirection(currentDx, currentDy);
        waitingMove = true;
    }
}
package io.github.maze.game;

import io.github.maze.entities.Player;
import io.github.maze.maze.GameObject;
import io.github.maze.maze.Maze;
import io.github.maze.obstacles.Portal;

public class AutoPlayer {

    private final Player player;
    private final GameObject[][] maze;
    private final String path;

    private int currentStep = 0;

    // logical tile position
    private int logicalX;
    private int logicalY;

    private int targetX;
    private int targetY;

    private int currentDx = 0;
    private int currentDy = 0;

    private boolean waitingMove = false;

    public AutoPlayer(Maze maze, String path) {
        this.player = maze.player;
        this.maze = maze.obstacleMap;
        this.path = path;

        this.player.setAutoMode(true);

        // get initial position
        this.logicalX = player.getTileX();
        this.logicalY = player.getTileY();
    }

    public void update() {

        if (path == null || path.isEmpty()) return;

        // wait until arrive at target
        if (waitingMove) {

            double playerX = player.getX();
            double playerY = player.getY();
            int realTileX = player.getTileX();
            int realTileY = player.getTileY();
            double targetPixelX = targetX * GamePanel.TILE_SIZE;
            double targetPixelY = targetY * GamePanel.TILE_SIZE;

            boolean reached = false;

            // Accurate vector threshold checking
            if (currentDx > 0 && playerX >= targetPixelX) reached = true;
            else if (currentDx < 0 && playerX <= targetPixelX) reached = true;
            else if (currentDy > 0 && playerY >= targetPixelY) reached = true;
            else if (currentDy < 0 && playerY <= targetPixelY) reached = true;

            if (!reached) {

                if (realTileX == targetX && realTileY == targetY) {
                    reached = true;
                }

                else if (Math.abs(realTileX - targetX) > 1 || Math.abs(realTileY - targetY) > 1) {
                    System.out.println("[PORTAL INTERCEPT] Player warped from target step!");

                    // Accept the warp destination directly as our new logical home base
                    logicalX = realTileX;
                    logicalY = realTileY;

                    waitingMove = false;
                    player.setAutoDirection(0, 0);
                    currentStep++;
                    return;
                }
            }

            if (reached) {

                player.setX(targetPixelX);
                player.setY(targetPixelY);

                logicalX = targetX;
                logicalY = targetY;

                // handle only objects within bounds
                if (logicalY >= 0 && logicalY < GamePanel.ROW_HEIGHT &&
                        logicalX >= 0 && logicalX < GamePanel.COL_WIDTH) {

                    GameObject currentObj = maze[logicalY][logicalX]; // Assuming maze[row][col] mapping

                    if (currentObj instanceof Portal portal) {

                        // Extract destination tile coordinates exactly like the solver does
                        int destCol = (int) (portal.getConnection().getX() / GamePanel.TILE_SIZE);
                        int destRow = (int) (portal.getConnection().getY() / GamePanel.TILE_SIZE);

                        // Teleport the physical player entity
                        player.setX(destCol * GamePanel.TILE_SIZE);
                        player.setY(destRow * GamePanel.TILE_SIZE);

                        // Sync the AutoPlayer's logical trackers to the destination
                        logicalX = destCol;
                        logicalY = destRow;

                        System.out.println("AutoPlayer successfully synced through portal to: " + logicalX + "," + logicalY);
                        System.out.println("player col=" + player.getTileX() + ", row=" + player.getTileY());
                    }
                }

                System.out.println(
                        "[ARRIVED] " +
                                "step=" + currentStep +
                                " target=(" + targetX + "," + targetY + ")" +
                                " actual=(" + player.getTileX() + "," + player.getTileY() + ")"
                );

                waitingMove = false;
                player.setAutoDirection(0, 0);

                currentStep++;

                return;
            }

            return;
        }

        // finish
        if (currentStep >= path.length()) {
            player.setAutoDirection(0, 0);
            player.setAutoMode(false);
            return;
        }

        int realTileX = player.getTileX();
        int realTileY = player.getTileY();

        if (logicalX != realTileX || logicalY != realTileY) {

            System.out.println(
                    "[AUTOPLAYER DESYNC] " +
                            "step=" + currentStep +
                            " logical=(" + logicalX + "," + logicalY + ")" +
                            " real=(" + realTileX + "," + realTileY + ")"
            );

            // resync
            logicalX = realTileX;
            logicalY = realTileY;
        }

        // print every move for debugging
        System.out.println(
                "[AUTOPLAYER] " +
                        "step=" + currentStep +
                        " logical=(" + logicalX + "," + logicalY + ")" +
                        " real=(" + realTileX + "," + realTileY + ")" +
                        " move=" + path.charAt(currentStep)
        );

        char move = path.charAt(currentStep);

        currentDx = 0;
        currentDy = 0;

        switch (move) {
            case 'U' -> currentDy = -1;
            case 'D' -> currentDy = 1;
            case 'L' -> currentDx = -1;
            case 'R' -> currentDx = 1;
        }

        // use logical position, not player pixel-derived tile
        targetX = logicalX + currentDx;
        targetY = logicalY + currentDy;

        System.out.println(
                "[TARGET] " +
                        "step=" + currentStep +
                        " from=(" + logicalX + "," + logicalY + ")" +
                        " to=(" + targetX + "," + targetY + ")" +
                        " move=" + move
        );

        player.setAutoDirection(currentDx, currentDy);
        waitingMove = true;
    }
}
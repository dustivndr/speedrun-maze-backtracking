package io.github.maze.game;

import io.github.maze.entities.Player;

public class AutoPlayer {

    private final Player player;
    private final String path;

    private int currentStep = 0;

    // 🔥 tracking target tile
    private int targetTileX;
    private int targetTileY;
    private boolean isMovingToTile = false;

    public AutoPlayer(Player player, String path) {
        this.player = player;
        this.path = path;

        player.setAutoMode(true);
    }

    public void update() {

        if (path == null || path.isEmpty())
            return;

        // 🔒 kalau masih menuju tile, tunggu selesai dulu
        if (isMovingToTile) {

            if (player.getTileX() == targetTileX &&
                player.getTileY() == targetTileY) {

                isMovingToTile = false;
                player.setAutoDirection(0, 0);
            }

            return;
        }

        // selesai semua langkah
        if (currentStep >= path.length()) {
            player.setAutoDirection(0, 0);
            player.setAutoMode(false);
            return;
        }

        char move = path.charAt(currentStep);

        int dx = 0, dy = 0;

        switch (move) {
            case 'U': dy = -1; break;
            case 'D': dy = 1; break;
            case 'L': dx = -1; break;
            case 'R': dx = 1; break;
        }

        // set target tile
        targetTileX = player.getTileX() + dx;
        targetTileY = player.getTileY() + dy;

        player.setAutoDirection(dx, dy);

        isMovingToTile = true;
        currentStep++;
    }
}
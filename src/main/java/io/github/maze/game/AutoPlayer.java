package io.github.maze.game;

import io.github.maze.entities.Player;

public class AutoPlayer {

    private final Player player;
    private final String path;

    private int currentStep = 0;

    private int targetX;
    private int targetY;

    private boolean waitingMove = false;

    public AutoPlayer(Player player, String path) {
        this.player = player;
        this.path = path;
        player.setAutoMode(true);
    }

    public void update() {

        if (path == null || path.isEmpty())
            return;

        // =========================
        // WAIT UNTIL ARRIVE TARGET
        // =========================
        if (waitingMove) {

            // sudah sampai tile target
            if (player.getTileX() == targetX &&
                player.getTileY() == targetY) {

                waitingMove = false;
                player.setAutoDirection(0, 0);
                currentStep++;
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

        int dx = 0, dy = 0;

        switch (move) {
            case 'U': dy = -1; break;
            case 'D': dy = 1; break;
            case 'L': dx = -1; break;
            case 'R': dx = 1; break;
        }

        // target tile yang harus dicapai
        targetX = player.getTileX() + dx;
        targetY = player.getTileY() + dy;

        player.setAutoDirection(dx, dy);

        waitingMove = true;
    }
}
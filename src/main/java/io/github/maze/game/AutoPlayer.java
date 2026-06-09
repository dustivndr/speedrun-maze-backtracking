package io.github.maze.game;

import io.github.maze.entities.Player;

public class AutoPlayer {

    private final Player player;
    private final String path;

    private int currentStep = 0;
    private long lastMove = 0;

    public AutoPlayer(Player player, String path) {
        this.player = player;
        this.path = path;

        player.setAutoMode(true);
    }

    public void update() {

        if (path == null || path.isEmpty())
            return;

        long now = System.currentTimeMillis();

        if (now - lastMove < 300)
            return;

        lastMove = now;

        if (currentStep >= path.length()) {

            player.setAutoDirection(0, 0);
            return;
        }

        char move = path.charAt(currentStep);

        switch (move) {

            case 'U':
                player.setAutoDirection(0, -1);
                break;

            case 'D':
                player.setAutoDirection(0, 1);
                break;

            case 'L':
                player.setAutoDirection(-1, 0);
                break;

            case 'R':
                player.setAutoDirection(1, 0);
                break;
        }

        currentStep++;
    }
}
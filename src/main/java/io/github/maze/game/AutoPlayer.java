package io.github.maze.game;

import io.github.maze.entities.Player;

public class AutoPlayer {

    private final Player player;
    private final String path;

    private int currentStep = 0;

    private int targetX;
    private int targetY;

    private int currentDx = 0;
    private int currentDy = 0;

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

            double targetPixelX = targetX * GamePanel.TILE_SIZE;
            double targetPixelY = targetY * GamePanel.TILE_SIZE;

            boolean reached = false;

            // Deteksi apakah koordinat pixel player sudah menyentuh/melewati target
            if (currentDx == 1 && player.getX() >= targetPixelX) reached = true;
            else if (currentDx == -1 && player.getX() <= targetPixelX) reached = true;
            else if (currentDy == 1 && player.getY() >= targetPixelY) reached = true;
            else if (currentDy == -1 && player.getY() <= targetPixelY) reached = true;

            if (reached) {
                // Paskan posisi (snap) secara manual dan akurat
                player.setX(targetPixelX);
                player.setY(targetPixelY);

                waitingMove = false;
                player.setAutoDirection(0, 0);
                currentStep++;
            }
            // Jika koordinat player tidak berubah sama sekali (nabrak tembok/out of bounds)
            else if (player.getX() == player.lastX && player.getY() == player.lastY) {
                player.setAutoDirection(0, 0);
                player.setAutoMode(false); // Batalkan AutoMode agar player bisa dikontrol manual lagi
                return;
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

        currentDx = 0;
        currentDx = 0;

        switch (move) {
            case 'U': currentDy = -1; break;
            case 'D': currentDy = 1; break;
            case 'L': currentDx = -1; break;
            case 'R': currentDx = 1; break;
        }

        // target tile yang harus dicapai
        targetX = player.getTileX() + currentDx;
        targetY = player.getTileY() + currentDy;

        player.setAutoDirection(currentDx, currentDy);

        waitingMove = true;
    }
}
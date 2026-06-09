package io.github.maze.maze.solver;

import java.util.Arrays;

public class MazeSolverBest {

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};
    private static final char[] DIR = {'U', 'D', 'L', 'R'};

    private static String bestPath;
    private static int bestRemainingHp;

    // Semi Dynamic Programming
    private static int[][] bestHpAtCell;

    public static String solve(
            char[][] maze,
            int startRow,
            int startCol,
            int startHp
    ) {

        int rows = maze.length;
        int cols = maze[0].length;

        bestPath = null;
        bestRemainingHp = -1;

        bestHpAtCell = new int[rows][cols];

        for (int r = 0; r < rows; r++) {
            Arrays.fill(bestHpAtCell[r], -1);
        }

        boolean[][] visited =
                new boolean[rows][cols];

        backtrack(
                maze,
                startRow,
                startCol,
                startHp,
                visited,
                new StringBuilder()
        );

        if (bestPath == null) {
            return "Path tidak ditemukan";
        }

        return bestPath;
    }

    private static void backtrack(
            char[][] maze,
            int row,
            int col,
            int hp,
            boolean[][] visited,
            StringBuilder currentPath
    ) {

        int rows = maze.length;
        int cols = maze[0].length;

        // Out of bound
        if (row < 0 || col < 0 ||
                row >= rows || col >= cols) {
            return;
        }

        // Tembok
        if (maze[row][col] == 'B') {
            return;
        }

        // HP habis
        if (hp <= 0) {
            return;
        }

        // Sudah dikunjungi di jalur saat ini
        if (visited[row][col]) {
            return;
        }

        // Semi Dynamic Programming
        if (hp <= bestHpAtCell[row][col]) {
            return;
        }

        bestHpAtCell[row][col] = hp;

        // Real Flag ditemukan
        if (maze[row][col] == 'F') {

            if (hp > bestRemainingHp) {

                bestRemainingHp = hp;
                bestPath = currentPath.toString();

            } else if (
                    hp == bestRemainingHp &&
                    bestPath != null &&
                    currentPath.length() < bestPath.length()
            ) {

                bestPath = currentPath.toString();
            }

            return;
        }

        visited[row][col] = true;

        for (int i = 0; i < 4; i++) {

            int nextRow = row + DR[i];
            int nextCol = col + DC[i];

            int nextHp = hp;

            if (nextRow >= 0 &&
                    nextCol >= 0 &&
                    nextRow < rows &&
                    nextCol < cols) {

                char tile = maze[nextRow][nextCol];

                /*
                 * Contoh damage tile
                 * Sesuaikan dengan map milikmu
                 */

                switch (tile) {

                    case 'S': // Spike
                        nextHp -= 10;
                        break;

                    case 'R': // Rock
                        nextHp -= 15;
                        break;

                    case 'L': // Lava
                        nextHp -= 25;
                        break;

                    case 'H': // Hole = langsung mati
                        nextHp = 0;
                        break;
                }
            }

            currentPath.append(DIR[i]);

            backtrack(
                    maze,
                    nextRow,
                    nextCol,
                    nextHp,
                    visited,
                    currentPath
            );

            // Undo langkah (Backtracking)
            currentPath.deleteCharAt(
                    currentPath.length() - 1
            );
        }

        visited[row][col] = false;
    }

    public static int getBestRemainingHp() {
        return bestRemainingHp;
    }
}
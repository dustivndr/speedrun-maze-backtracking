package io.github.maze.maze.solver;

public class MazeSolverBest {

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};
    private static final char[] DIR = {'U', 'D', 'L', 'R'};

    private static String bestPath;
    private static int bestRemainingHp;
    private static int totalFlags;

    public static String solve(
            char[][] maze,
            int startRow,
            int startCol,
            int startHp
    ) {

        bestPath = null;
        bestRemainingHp = -1;
        totalFlags = 0;

        for (int r = 0; r < maze.length; r++) {
            for (int c = 0; c < maze[0].length; c++) {

                if (maze[r][c] == 'F' ||
                    maze[r][c] == 'L') {

                    totalFlags++;
                }
            }
        }

        boolean[][] visited =
                new boolean[maze.length][maze[0].length];

        backtrack(
                maze,
                startRow,
                startCol,
                startHp,
                0,              // keys
                0,              // flags collected
                visited,
                new StringBuilder()
        );

        return bestPath == null
                ? "Path tidak ditemukan"
                : bestPath;
    }

    private static void backtrack(
            char[][] maze,
            int row,
            int col,
            int hp,
            int keys,
            int flagsCollected,
            boolean[][] visited,
            StringBuilder currentPath
    ) {

        int rows = maze.length;
        int cols = maze[0].length;

        // out of bounds
        if (row < 0 ||
            col < 0 ||
            row >= rows ||
            col >= cols) {

            return;
        }

        char tile = maze[row][col];

        // obstacle
        if (tile == 'B') {
            return;
        }

        // sudah dikunjungi
        if (visited[row][col]) {
            return;
        }

        // =====================
        // DAMAGE
        // =====================

        switch (tile) {

            case 'S':
                hp -= 5;
                break;

            case 'N':
                hp -= 5;
                break;

            case 'R':
                hp -= 5;
                break;

            case 'W':
                hp -= 10;
                break;

            case 'M':
                hp -= 20;
                break;

            case 'H':
                hp -= 5;
                break;
        }

        if (hp <= 0) {
            return;
        }

        // backup tile untuk restore
        char originalTile = tile;

        // =====================
        // KEY
        // =====================

        if (tile == 'K') {

            keys++;

            // supaya tidak diambil lagi
            maze[row][col] = '0';
        }

        // =====================
        // FLAG GREEN
        // =====================

        if (tile == 'F') {

            flagsCollected++;

            maze[row][col] = '0';
        }

        // =====================
        // FLAG LOCKED
        // =====================

        if (tile == 'L') {

            if (keys <= 0) {

                maze[row][col] = originalTile;
                return;
            }

            keys--;

            flagsCollected++;

            maze[row][col] = '0';
        }

        // =====================
        // GOAL
        // =====================

        if (flagsCollected == totalFlags) {

            if (hp > bestRemainingHp) {

                bestRemainingHp = hp;
                bestPath = currentPath.toString();

            }
            else if (hp == bestRemainingHp &&
                    (bestPath == null ||
                     currentPath.length() < bestPath.length())) {

                bestPath = currentPath.toString();
            }

            maze[row][col] = originalTile;
            return;
        }

        visited[row][col] = true;

        // =====================
        // RECURSIVE SEARCH
        // =====================

        for (int i = 0; i < 4; i++) {

            currentPath.append(DIR[i]);

            backtrack(
                    maze,
                    row + DR[i],
                    col + DC[i],
                    hp,
                    keys,
                    flagsCollected,
                    visited,
                    currentPath
            );

            currentPath.deleteCharAt(
                    currentPath.length() - 1
            );
        }

        visited[row][col] = false;

        // restore tile
        maze[row][col] = originalTile;
    }

    public static int getBestRemainingHp() {
        return bestRemainingHp;
    }
}
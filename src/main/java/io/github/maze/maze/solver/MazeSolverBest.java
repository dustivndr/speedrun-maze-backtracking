package io.github.maze.maze.solver;

public class MazeSolverBest {

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};
    private static final char[] DIR = {'U', 'D', 'L', 'R'};

    private static String bestPath;
    private static int bestRemainingHp;

    public static String solve(
            char[][] maze,
            int startRow,
            int startCol,
            int startHp
    ) {

        bestPath = null;
        bestRemainingHp = -1;

        boolean[][] visited = new boolean[maze.length][maze[0].length];

        backtrack(
                maze,
                startRow,
                startCol,
                startHp,
                visited,
                new StringBuilder()
        );

        return (bestPath == null) ? "Path tidak ditemukan" : bestPath;
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

        // out of bounds
        if (row < 0 || col < 0 || row >= rows || col >= cols) return;

        // tembok
        if (maze[row][col] == 'B') return;

        // sudah dikunjungi di jalur ini
        if (visited[row][col]) return;

        // apply damage di tile sekarang
        char tile = maze[row][col];
        switch (tile) {
            case 'S': hp -= 10; break;
            case 'R': hp -= 15; break;
            case 'L': hp -= 25; break;
            case 'H': hp = 0; break;
        }

        // mati setelah kena tile
        if (hp <= 0) return;

        // ✅ GOAL CHECK (SETELAH DAMAGE)
        if (tile == 'F') {
            if (hp > bestRemainingHp) {
                bestRemainingHp = hp;
                bestPath = currentPath.toString();
            } else if (hp == bestRemainingHp &&
                    bestPath != null &&
                    currentPath.length() < bestPath.length()) {
                bestPath = currentPath.toString();
            }
            return;
        }

        visited[row][col] = true;

        for (int i = 0; i < 4; i++) {

            int nextRow = row + DR[i];
            int nextCol = col + DC[i];

            currentPath.append(DIR[i]);

            backtrack(
                    maze,
                    nextRow,
                    nextCol,
                    hp,
                    visited,
                    currentPath
            );

            // undo langkah
            currentPath.deleteCharAt(currentPath.length() - 1);
        }

        visited[row][col] = false;
    }

    public static int getBestRemainingHp() {
        return bestRemainingHp;
    }
}
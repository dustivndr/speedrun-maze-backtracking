public class MazeSolverBest {

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};
    private static final char[] DIR = {'U', 'D', 'L', 'R'};

    private static String bestPath;

    public static String solve(char[][] maze) {

        int rows = maze.length;
        int cols = maze[0].length;

        int startRow = -1;
        int startCol = -1;

        // Cari posisi start
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (maze[r][c] == 'P') {
                    startRow = r;
                    startCol = c;
                }
            }
        }

        bestPath = null;

        boolean[][] visited = new boolean[rows][cols];

        dfs(
            maze,
            startRow,
            startCol,
            visited,
            new StringBuilder()
        );

        return bestPath == null
                ? "Path tidak ditemukan"
                : bestPath;
    }

    private static void dfs(
            char[][] maze,
            int row,
            int col,
            boolean[][] visited,
            StringBuilder currentPath) {

        int rows = maze.length;
        int cols = maze[0].length;

        if (row < 0 || col < 0 ||
            row >= rows || col >= cols) {
            return;
        }

        if (maze[row][col] == 'B') {
            return;
        }

        if (visited[row][col]) {
            return;
        }

        // Pruning
        if (bestPath != null &&
            currentPath.length() >= bestPath.length()) {
            return;
        }

        // Goal ditemukan
        if (maze[row][col] == 'F') {
            bestPath = currentPath.toString();
            return;
        }

        visited[row][col] = true;

        for (int i = 0; i < 4; i++) {

            currentPath.append(DIR[i]);

            dfs(
                maze,
                row + DR[i],
                col + DC[i],
                visited,
                currentPath
            );

            currentPath.deleteCharAt(
                currentPath.length() - 1
            );
        }

        visited[row][col] = false;
    }
}

package io.github.maze.maze.solver;

import java.util.LinkedList;
import java.util.Queue;

public class MazeSolver {

    static class Node {
        int row;
        int col;
        String path;

        Node(int row, int col, String path) {
            this.row = row;
            this.col = col;
            this.path = path;
        }
    }

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};
    private static final char[] DIR = {'U', 'D', 'L', 'R'};

    public static String solve(char[][] maze) {

        int rows = maze.length;
        int cols = maze[0].length;

        int startRow = -1;
        int startCol = -1;

        // Cari posisi start (P)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (maze[r][c] == 'P') {
                    startRow = r;
                    startCol = c;
                }
            }
        }

        boolean[][] visited = new boolean[rows][cols];
        Queue<Node> queue = new LinkedList<>();

        queue.add(new Node(startRow, startCol, ""));
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            // Goal ditemukan
            if (maze[current.row][current.col] == 'F') {
                return current.path;
            }

            for (int i = 0; i < 4; i++) {

                int newRow = current.row + DR[i];
                int newCol = current.col + DC[i];

                if (newRow < 0 || newCol < 0 ||
                    newRow >= rows || newCol >= cols) {
                    continue;
                }

                if (maze[newRow][newCol] == 'B') {
                    continue;
                }

                if (visited[newRow][newCol]) {
                    continue;
                }

                visited[newRow][newCol] = true;

                queue.add(
                    new Node(
                        newRow,
                        newCol,
                        current.path + DIR[i]
                    )
                );
            }
        }

        return "Path tidak ditemukan";
    }
}

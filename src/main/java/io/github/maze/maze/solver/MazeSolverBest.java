package io.github.maze.maze.solver;

import java.util.*;

public class MazeSolverBest {

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};
    private static final char[] DIR = {'U', 'D', 'L', 'R'};

    private static String bestPath;
    private static int bestRemainingHp;
    private static int totalFlags;

    private static Map<Character, List<int[]>> portalMap;

    // ===================== MEMOIZATION (DP CACHE) =====================
    private static Map<String, Integer> memo;

    public static String solve(char[][] maze, int sr, int sc, int startHp) {

        bestPath = null;
        bestRemainingHp = -1;
        totalFlags = countFlags(maze);
        portalMap = buildPortals(maze);

        memo = new HashMap<>();

        boolean[][] visited = new boolean[maze.length][maze[0].length];

        backtrack(
                maze,
                sr,
                sc,
                startHp,
                0,
                0,
                0,
                0,
                0,
                new HashSet<>(),
                new StringBuilder(),
                visited
        );

        return bestPath == null ? "Path tidak ditemukan" : bestPath;
    }

    // ========================= BACKTRACK + DP =========================

    private static void backtrack(
            char[][] maze,
            int r,
            int c,
            int hp,
            int keys,
            int flags,
            int tileCount,
            int poisonTimer,
            int speedTimer,
            Set<String> holeBroken,
            StringBuilder path,
            boolean[][] visited
    ) {

        if (!inBounds(maze, r, c)) return;
        if (visited[r][c]) return;

        char tile = maze[r][c];

        if (isSolid(tile, r, c, holeBroken)) return;

        // ================= SPEED =================
        boolean speedActive = speedTimer > 0;
        tileCount += speedActive ? 2 : 1;

        // ================= POISON =================
        if (poisonTimer > 0) {
            hp -= 1;
            poisonTimer--;
        }

        if (hp <= 0) return;

        char original = tile;

        // ================= TILE EFFECTS =================
        if (tile == 'H') {
            hp -= 5;
            maze[r][c] = 'B';
            holeBroken.add(r + "," + c);
        }

        if (tile == 'K') {
            keys++;
            maze[r][c] = '0';
        }

        if (tile == 'F' || tile == 'f') {
            flags++;
            maze[r][c] = '0';
        }

        if (tile == 'L') {
            if (keys <= 0) return;
            keys--;
            flags++;
            maze[r][c] = '0';
        }

        if (tile == 'h') {
            hp += 20;
            maze[r][c] = '0';
        }

        if (tile == 's') {
            speedTimer = 20;
            maze[r][c] = '0';
        }

        if (tile == 'p') {
            poisonTimer = 10;
            maze[r][c] = '0';
        }

        if (tile == 'E') {
            hp = 1000;
        }

        if (tile == 'O') {
            int[] dest = teleport(r, c, maze);
            r = dest[0];
            c = dest[1];
        }

        // ================= WIN =================
        if (flags == totalFlags) {
            if (hp > bestRemainingHp) {
                bestRemainingHp = hp;
                bestPath = path.toString();
            }
            maze[r][c] = original;
            return;
        }

        visited[r][c] = true;

        // ================= DP KEY =================
        String stateKey = r + "," + c + "," + keys + "," + flags + "," + poisonTimer + "," + speedTimer;

        if (memo.containsKey(stateKey)) {
            if (memo.get(stateKey) >= hp) {
                visited[r][c] = false;
                maze[r][c] = original;
                return;
            }
        }
        memo.put(stateKey, hp);

        // ================= ENEMY =================
        hp = applyEnemyDamage(maze, r, c, hp, tileCount);

        if (hp <= 0) {
            visited[r][c] = false;
            maze[r][c] = original;
            return;
        }

        // ================= MOVE =================
        for (int i = 0; i < 4; i++) {

            path.append(DIR[i]);

            backtrack(
                    maze,
                    r + DR[i],
                    c + DC[i],
                    hp,
                    keys,
                    flags,
                    tileCount,
                    poisonTimer,
                    speedTimer > 0 ? speedTimer - 1 : 0,
                    holeBroken,
                    path,
                    visited
            );

            path.deleteCharAt(path.length() - 1);
        }

        visited[r][c] = false;
        maze[r][c] = original;
    }

    // ================= SAME FUNCTIONS (unchanged) =================

    private static int applyEnemyDamage(char[][] maze, int r, int c, int hp, int step) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {

                char t = maze[i][j];

                if (t == 'N' && inRange(i, j, r, c, 3)) {
                    if (step % 2 == 0) hp -= 5;
                }

                if (t == 'W' && inRange(i, j, r, c, 5)) {
                    if (step % 4 == 0) hp -= 10;
                }

                if (t == 'M' && inRange(i, j, r, c, 6, 5)) {
                    hp -= 20;
                }
            }
        }
        return hp;
    }

    private static boolean isSolid(char t, int r, int c, Set<String> holeBroken) {
        if (t == 'B') return true;
        if (t == 'N' || t == 'W' || t == 'M') return true;
        if (t == 'H') return holeBroken.contains(r + "," + c);
        return false;
    }

    private static boolean inRange(int r1, int c1, int r2, int c2, int range) {
        return Math.abs(r1 - r2) <= range && Math.abs(c1 - c2) <= range;
    }

    private static boolean inRange(int r1, int c1, int r2, int c2, int rx, int ry) {
        return Math.abs(r1 - r2) <= rx && Math.abs(c1 - c2) <= ry;
    }

    private static int[] teleport(int r, int c, char[][] maze) {
        char id = maze[r][c];
        List<int[]> list = portalMap.get(id);

        if (list.size() < 2) return new int[]{r, c};

        return (list.get(0)[0] == r && list.get(0)[1] == c)
                ? list.get(1)
                : list.get(0);
    }

    private static Map<Character, List<int[]>> buildPortals(char[][] maze) {
        Map<Character, List<int[]>> map = new HashMap<>();
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == 'O') {
                    map.computeIfAbsent('O', k -> new ArrayList<>())
                            .add(new int[]{i, j});
                }
            }
        }
        return map;
    }

    private static boolean inBounds(char[][] m, int r, int c) {
        return r >= 0 && c >= 0 && r < m.length && c < m[0].length;
    }

    private static int countFlags(char[][] maze) {
        int count = 0;
        for (char[] row : maze)
            for (char c : row)
                if (c == 'F' || c == 'L' || c == 'f')
                    count++;
        return count;
    }

    public static int getBestRemainingHp() {
        return bestRemainingHp;
    }
}
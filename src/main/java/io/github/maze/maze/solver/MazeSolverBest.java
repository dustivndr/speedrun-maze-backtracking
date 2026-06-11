// package io.github.maze.maze.solver;

// import java.util.*;

// public class MazeSolverBest {

//     private static final int[] DR = {-1, 1, 0, 0};
//     private static final int[] DC = {0, 0, -1, 1};
//     private static final char[] DIR = {'U', 'D', 'L', 'R'};

//     private static String bestPath = "";
//     private static int bestRemainingHp = -1;

//     private static char[][] maze;
//     private static int rows;
//     private static int cols;

//     private static Map<String, Integer> memo;

//     private static List<Point> flags;
//     private static List<Point> lockedFlags;
//     private static List<Point> keys;
//     private static List<Point> holes;

//     private static int allFlagsMask;

//     public static String solve(char[][] map) {

//         maze = map;
//         rows = map.length;
//         cols = map[0].length;

//         memo = new HashMap<>();

//         flags = new ArrayList<>();
//         lockedFlags = new ArrayList<>();
//         keys = new ArrayList<>();
//         holes = new ArrayList<>();

//         int startRow = -1;
//         int startCol = -1;

//         scanMap();

//         for (int r = 0; r < rows; r++) {
//             for (int c = 0; c < cols; c++) {

//                 if (maze[r][c] == 'P') {
//                     startRow = r;
//                     startCol = c;
//                 }
//             }
//         }

//         allFlagsMask =
//                 (1 << (flags.size() + lockedFlags.size())) - 1;

//         bestPath = "";
//         bestRemainingHp = -1;

//         State start = new State();

//         start.row = startRow;
//         start.col = startCol;

//         start.hp = 100;

//         start.keys = 0;

//         start.flagMask = 0;
//         start.keyMask = 0;
//         start.holeMask = 0;

//         start.path = "";

//         dfs(start);

//         return bestPath;
//     }

//     private static void dfs(State s) {

//         if (s.hp <= 0)
//             return;

//         if (s.flagMask == allFlagsMask) {

//             if (s.hp > bestRemainingHp) {
//                 bestRemainingHp = s.hp;
//                 bestPath = s.path;
//             }

//             return;
//         }

//         String stateKey =
//                 s.row + "," +
//                 s.col + "," +
//                 s.hp + "," +
//                 s.keys + "," +
//                 s.flagMask + "," +
//                 s.keyMask + "," +
//                 s.holeMask;

//         Integer prev = memo.get(stateKey);

//         if (prev != null && prev >= s.hp)
//             return;

//         memo.put(stateKey, s.hp);

//         for (int d = 0; d < 4; d++) {

//             State next = move(s, d);

//             if (next != null)
//                 dfs(next);
//         }
//     }

//     private static State move(State s, int dir) {

//         int nr = s.row + DR[dir];
//         int nc = s.col + DC[dir];

//         if (!inside(nr, nc))
//             return null;

//         char tile = maze[nr][nc];

//         if (tile == 'B')
//             return null;

//         State n = s.copy();

//         n.row = nr;
//         n.col = nc;

//         n.path += DIR[dir];

//         switch (tile) {

//             case 'S':
//                 n.hp -= 5;
//                 break;

//             case 'R':
//                 n.hp -= 5;
//                 break;

//             case 'h':
//                 n.hp = Math.min(100, n.hp + 20);
//                 break;

//             case 'K':

//                 int keyId = getKeyId(nr, nc);

//                 if (keyId >= 0 &&
//                         (n.keyMask & (1 << keyId)) == 0) {

//                     n.keyMask |= (1 << keyId);
//                     n.keys++;
//                 }

//                 break;

//             case 'H':

//                 int holeId = getHoleId(nr, nc);

//                 if ((n.holeMask & (1 << holeId)) != 0)
//                     return null;

//                 n.holeMask |= (1 << holeId);

//                 n.hp -= 5;

//                 break;

//             case 'F':

//                 int flagId = getFlagId(nr, nc);

//                 if (flagId >= 0)
//                     n.flagMask |= (1 << flagId);

//                 break;

//             case 'f':

//                 break;

//             case 'L':

//                 int lockedId = getLockedFlagId(nr, nc);

//                 if (lockedId >= 0) {

//                     int bit =
//                             1 << (flags.size() + lockedId);

//                     if ((n.flagMask & bit) == 0) {

//                         if (n.keys <= 0)
//                             return null;

//                         n.keys--;

//                         n.flagMask |= bit;
//                     }
//                 }

//                 break;

//             case 'O':

//                 Point p = findConnectedPortal(nr, nc);

//                 if (p != null) {

//                     n.row = p.row;
//                     n.col = p.col;
//                 }

//                 break;
//         }

//         if (n.hp <= 0)
//             return null;

//         return n;
//     }

//     private static void scanMap() {

//         for (int r = 0; r < rows; r++) {

//             for (int c = 0; c < cols; c++) {

//                 switch (maze[r][c]) {

//                     case 'F':
//                         flags.add(new Point(r, c));
//                         break;

//                     case 'L':
//                         lockedFlags.add(new Point(r, c));
//                         break;

//                     case 'K':
//                         keys.add(new Point(r, c));
//                         break;

//                     case 'H':
//                         holes.add(new Point(r, c));
//                         break;
//                 }
//             }
//         }
//     }

//     private static int getFlagId(int r, int c) {

//         for (int i = 0; i < flags.size(); i++) {

//             Point p = flags.get(i);

//             if (p.row == r && p.col == c)
//                 return i;
//         }

//         return -1;
//     }

//     private static int getLockedFlagId(int r, int c) {

//         for (int i = 0; i < lockedFlags.size(); i++) {

//             Point p = lockedFlags.get(i);

//             if (p.row == r && p.col == c)
//                 return i;
//         }

//         return -1;
//     }

//     private static int getKeyId(int r, int c) {

//         for (int i = 0; i < keys.size(); i++) {

//             Point p = keys.get(i);

//             if (p.row == r && p.col == c)
//                 return i;
//         }

//         return -1;
//     }

//     private static int getHoleId(int r, int c) {

//         for (int i = 0; i < holes.size(); i++) {

//             Point p = holes.get(i);

//             if (p.row == r && p.col == c)
//                 return i;
//         }

//         return -1;
//     }

//     private static Point findConnectedPortal(int r, int c) {

//         for (int rr = 0; rr < rows; rr++) {

//             for (int cc = 0; cc < cols; cc++) {

//                 if (maze[rr][cc] == 'O'
//                         && (rr != r || cc != c))
//                     return new Point(rr, cc);
//             }
//         }

//         return null;
//     }

//     private static boolean inside(int r, int c) {

//         return r >= 0 &&
//                 r < rows &&
//                 c >= 0 &&
//                 c < cols;
//     }

//     private static class State {

//         int row;
//         int col;

//         int hp;

//         int keys;

//         int flagMask;
//         int keyMask;
//         int holeMask;

//         String path;

//         State copy() {

//             State s = new State();

//             s.row = row;
//             s.col = col;

//             s.hp = hp;

//             s.keys = keys;

//             s.flagMask = flagMask;
//             s.keyMask = keyMask;
//             s.holeMask = holeMask;

//             s.path = path;

//             return s;
//         }
//     }

//     private static class Point {

//         int row;
//         int col;

//         Point(int row, int col) {

//             this.row = row;
//             this.col = col;
//         }
//     }
// }
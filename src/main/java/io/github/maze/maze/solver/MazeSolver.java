package io.github.maze.maze.solver;

import java.util.*;

import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import io.github.maze.obstacles.*;
import io.github.maze.util.Point;

public class MazeSolver {

    private static class Result {
        State state;
        int hp;

        public Result(State state, int hp) {
            this.state= state;
            this.hp = hp;
        }
    }

    private static final int[] DR = {-1, 1, 0, 0}; // DELTA ROW: perubahan row (kiri = -1, kanan = 1, tetap = 0)
    private static final int[] DC = {0, 0, -1, 1}; // DELTA COL: perubahan col (naik = -1, turun = 1, tetap = 0)
    private static final char[] DIR = {'U', 'D', 'L', 'R'}; // char pilihan pergerakan player

    private static int totalFlag; // jumlah flag yang perlu di ambil spy menang
    private static String bestPath; // jalan terbaik dengan panjang maksimal tertentu
    public static int bestRemainingHp; //
    public static StringBuilder backtrackingPath = new StringBuilder(3000000);

    private static Map<State, int[]> memo;

    /*
     * goal: dapatkan semua flag dengan sebanyak mungkin hp
     *
     * @param gp
     * @return
     */
    public static String solve(GamePanel gp) {

        // titik starting player
        Point pos = new Point(gp.maze.player.getTileY(), gp.maze.player.getTileX());
        State initialState;

        totalFlag = gp.maze.flagCount; // menyimpan total flag untuk win case
        memo = new HashMap<>(); // memo untuk backtracking
        StringBuilder path = new StringBuilder(); // membuat string dengan efisien

        // nge loop dengan maksimal panjang path.
        // ini dilakukan karena semisal map tidak memiliki obstacle
        // dan flag tepat di sebelahnya kita membatasi panjang path ke maxDepth
        // agar tidak mbulet jalan e karena algorithma bisa memutari
        // map 100 kali atau langsung lurus ke goal jika urutan pencobaan
        // backtracking tidak sesuai
        for (int maxDepth = 20; maxDepth <= 2000; maxDepth += 20) {

            // reset data tiap kali mencoba depth baru
            path.setLength(0); // perjalanan algoritma jadikan ""
            memo.clear(); // bersihkan memo
            bestRemainingHp = -1; // reset hp
            bestPath = ""; // reset hasil jalan terbaik pass backtracking ini
            backtrackingPath.setLength(0);

            initialState = initState(pos);

            backtracking(
                    gp.maze.obstacleMap,
                    initialState,
                    100,
                    path,
                    maxDepth,
                    ' ',
                    initialState.position()
            );

            if (!bestPath.isEmpty()) {
                System.out.println("PATH = " + bestPath);
                return bestPath;
            }
        }

        return bestPath;
    }

    private static State initState(Point point) {
        return new State(
                point,
                0,
                0,
                0,
                0,
                0,
                0,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashMap<>(),
                new HashMap<>(),
                new HashSet<>()
        );
    }

    private static void backtracking(
            GameObject[][] maze,
            State state,
            int hp,
            StringBuilder path,
            int maxDepth,
            char dir,
            Point previousActualPos
    ) {

        // base case
        if (
                !bestPath.isEmpty() && //  udah ketemu best path
                bestRemainingHp == 100 && // udh ketemu full hp lngsng stop
                path.length() >= bestPath.length() // batasi panjang path algoritma spy gk kepanjangen
        ) {
            return;
        }

        // membatasi scope pencobaan backtracking agar tidak mencoba terlalu banyak
        // dan nge StackOverflowError dimana memory program tidak cukup
        if (path.length() > maxDepth) {
            return;
        }

        // check out of bound
        if (!inBound(state)) return;

        // handle tile effects:
        // spike, fire, hole, key, flagGreen, flagLocked, healSpell, poisonSpell, speedSpell
        Result tileResult = processTile(maze, state, hp);

        // update player:
        //
        // speedRemaining
        // walkCount
        // poisonRemaining
        // poison damage
        Result playerUpdateResult = updateWalkCountAndStatusEffects(tileResult);
        state = playerUpdateResult.state;
        hp = playerUpdateResult.hp;

        // check if player is able to move into that tile or not
        if (isBlocked(maze, state)) return;

        // apply elf healing
        hp = applyElfHealing(maze, state.position(), hp);

        // apply enemy damage
        // this updates: ninjaEntryWalkCount, wizardEntryWalkCount, fireMonstersTriggered
        Result enemyDamageResult = applyEnemyDamage(maze, state, hp);
        state = enemyDamageResult.state;
        hp = enemyDamageResult.hp;

        // check lose (lose base case)
        if (hp <= 0) return;

        // remove unnecessary branches
        if (shouldPrune(state, hp, path.length())) return;

        // add position to path taken
        boolean isRoot = (dir == ' ');
        if (!isRoot) {
            path.append(dir);
            backtrackingPath.append(dir);
        }

        // check win
        if (state.collectedGreenFlags() == totalFlag) {

//            System.out.println(
//                    state.position()
//                            + " flagsSet=" + state.collectedFlags()
//            );

            // berhasil ngambil semua flag.
            // cek apakah path yang terbaik? dan ambil yang terbaik
            // jika sama kebaikannya, ambil path yg sebelumnya
            if (hp > bestRemainingHp ||
                    (hp == bestRemainingHp &&
                        (bestPath.isEmpty() || path.length() < bestPath.length()))) {
                bestRemainingHp = hp;
                bestPath = path.toString();
            }

            // backtrack path
            if (!isRoot) {
                path.deleteCharAt(path.length() - 1);
                char rev = getExactReturnChar(state.position(), previousActualPos, dir);
                if (rev != ' ') backtrackingPath.append(rev);
            }

            // win base case
            return;
        }

        // mencoba ke 4 arah player gerak
        // i == 0: ke atas
        // i == 1: ke bawah
        // i == 2: ke kiri
        // i == 3: ke kanan
        for (int i = 0; i < 4; i++) {

            // get current position
            Point curr = state.position();

            // get next position
            Point nextPos = new Point(curr.row() + DR[i], curr.col() + DC[i]);

            State nextState = new State(
                nextPos,
                state.keyCount(),
                state.collectedGreenFlags(),
                state.walkCount(),
                state.tilesWalkedWithSpeed(),
                state.fireRemaining(),
                state.speedRemaining(),
                state.collectedKeys(),
                state.collectedFlags(),
                state.collectedSpells(),
                state.brokenHoles(),
                state.ninjasPlayerInside(),
                state.wizardsPlayerInside(),
                state.fireMonstersPlayerInside()
            );

            if (!inBound(nextState) || isBlocked(maze, nextState)) {
                continue;
            }

            // backtrack the next position
            backtracking(
                    maze,
                    nextState,
                    hp,
                    path,
                    maxDepth,
                    DIR[i],
                    state.position()
            );
        }

        if (!isRoot) {
            path.deleteCharAt(path.length() - 1);
            char rev = getExactReturnChar(state.position(), previousActualPos, dir);
            if (rev != ' ') {
                backtrackingPath.append(rev);
            }
        }
    }

    private static char getExactReturnChar(Point currentPos, Point targetPos, char incomingDir) {
        int dRow = targetPos.row() - currentPos.row();
        int dCol = targetPos.col() - currentPos.col();

        // inverse movement
        if (Math.abs(dRow) + Math.abs(dCol) == 1) {
            if (dRow == -1) return 'U';
            if (dRow == 1) return 'D';
            if (dCol == -1) return 'L';
            if (dCol == 1) return 'R';
        }
        return ' ';
    }

    private static boolean shouldPrune(
            State state,
            int hp,
            int pathLength
    ) {
        // if hp in memo is more than the current HP,
        // don't prune
        if (memo.containsKey(state)) {

            int[] best = memo.get(state);
            int bestHp = best[0];
            int bestPathLength = best[1];

            if (bestHp > hp) {
                return true;
            }

            if (bestHp == hp && bestPathLength <= pathLength) {
                return true;
            }

            best[0] = hp;
            best[1] = pathLength;
            return false;
        }

        // else prune
        memo.put(state, new int[]{hp, pathLength});
        return false;
    }

    private static Result applyEnemyDamage(
            GameObject[][] maze,
            State state,
            int hp
    ) {
        Point position = state.position(); // posisi playernya sekarang ini
        int startRow = Math.max(position.row() - 2, 0); //
        int startCol = Math.max(position.col() - 3, 0);
        int endRow = Math.min(startRow + 4, GamePanel.ROW_HEIGHT - 1);
        int endCol = Math.min(startCol + 6, GamePanel.COL_WIDTH - 1);

        Set<Point> fireMonstersPlayerInside = new HashSet<>(state.fireMonstersPlayerInside());
        Set<Point> fmsInside = new HashSet<>(fireMonstersPlayerInside.size());

        Map<Point, Integer> wizardsPlayerInside = new HashMap<>(state.wizardsPlayerInside());
        Set<Point> wsInside = new HashSet<>(wizardsPlayerInside.size());

        Map<Point, Integer> ninjasPlayerInside = new HashMap<>(state.ninjasPlayerInside());
        Set<Point> nsInside = new HashSet<>(ninjasPlayerInside.size());

        // TODO: apply speed for move count in range

        for (int r = startRow; r <= endRow; r++) {
            for (int c = startCol; c <= endCol; c++) {

                GameObject obj = maze[r][c];
                Point pos = new Point(r, c);

                // check current position for FireMOnster
                if (obj instanceof FireMonster fm &&

                        // player in range of FireMonster
                        position.row() >= r - 2 &&
                        position.row() <= r + 2 &&
                        position.col() >= c - 2 &&
                        position.col() <= c + 3
                ) {

                    fmsInside.add(pos);

                    // if player isn't already inside the FireMonster, attack
                    if (!fireMonstersPlayerInside.contains(pos)) {
                        fireMonstersPlayerInside.add(pos);
                        hp -= 20;
                    }
                }

                // check current position for Wizard
                if (
                        obj instanceof Wizard w &&

                        // range of wizard
                        position.row() >= r - 2 &&
                        position.row() <= r + 2 &&
                        position.col() >= c - 2 &&
                        position.col() <= c + 2
                ) {

                    wsInside.add(pos);

                    // player has just entered wizard range
                    if (!wizardsPlayerInside.containsKey(pos)) {
                        wizardsPlayerInside.put(pos, state.walkCount());
                        hp -= 10;
                    }

                    // player was already within wizard range
                    else {

                        // check for tile moves
                        int walkCountOnEntry = wizardsPlayerInside.get(pos);

                        // move count in wizard range is a multiple of 4
                        if ((state.walkCount() - walkCountOnEntry) % 4 == 0) {
                            hp -= 10;
                        }
                    }
                }

                // check current position for Ninja
                if (
                        obj instanceof Ninja &&

                        // check if player is in Ninja range
                        position.row() >= r - 1 &&
                        position.row() <= r + 1 &&
                        position.col() >= c - 1 &&
                        position.col() <= c + 1
                ) {
                    nsInside.add(pos);

                    // player has just entered Ninja range
                    if (!ninjasPlayerInside.containsKey(pos)) {
                        ninjasPlayerInside.put(pos, state.walkCount());
                        hp -= 5;
                    }

                    // player was already in NInja range
                    else {

                        int walkCountOnEntry = ninjasPlayerInside.get(pos);

                        // move count is a multiple of 2
                        if ((state.walkCount() - walkCountOnEntry) % 2 == 0) {
                            hp -= 5;
                        }
                    }
                }
            }
        }

        hp = Math.clamp(hp, 0, 100);

        // removes all enemies which doesn't have the player in its range anymore
        ninjasPlayerInside.keySet().retainAll(nsInside);
        wizardsPlayerInside.keySet().retainAll(wsInside);
        fireMonstersPlayerInside.retainAll(fmsInside);

        State newState = new State(
                state.position(),
                state.keyCount(),
                state.collectedGreenFlags(),
                state.walkCount(),
                state.tilesWalkedWithSpeed(),
                state.fireRemaining(),
                state.speedRemaining(),
                state.collectedKeys(),
                state.collectedFlags(),
                state.collectedSpells(),
                state.brokenHoles(),
                ninjasPlayerInside,
                wizardsPlayerInside,
                fireMonstersPlayerInside
        );

        return new Result(newState, hp);
    }

    // klo ada elf di ukuran 3x3 heal ke full hp (100)
    private static int applyElfHealing(
            GameObject[][] maze,
            Point position,
            int hp
    ) {
        for (int r = Math.max(position.row() - 1, 0); r <= Math.min(GamePanel.ROW_HEIGHT - 1, position.row() + 1); r++) {
            for (int c = Math.max(position.col() - 1, 0); c <= Math.min(position.col() + 1, GamePanel.COL_WIDTH - 1); c++) {
                if (maze[r][c] instanceof Elf) {
                    return 100;
                }
            }
        }

        return hp;
    }

    // exactly teh same as update method in Player class
    private static Result updateWalkCountAndStatusEffects(
            Result result
    ) {
        State state = result.state;
        int hp = result.hp;

        int speedLength = state.speedRemaining();
        int poisonLength = state.fireRemaining();
        int walkCount = state.walkCount();
        int tilesWalkedWithSpeed = state.tilesWalkedWithSpeed();

        //
        if (speedLength > 0) {

            tilesWalkedWithSpeed++;

            // karena speed player di 2x, anggep player
            // gerak 1 tile tiap gerak 2 tile.
            if (tilesWalkedWithSpeed % 2 == 0) {

                // damage juga tiap 2 tile hanya jika player punya efek speed
                if (poisonLength > 0) {
                    hp--;
                    poisonLength--;
                }

                walkCount++;
            }

            speedLength--;

            // reset jumlah player gerak jika speed habis
            if (speedLength <= 0) {
                tilesWalkedWithSpeed = 0;
            }

        } else {

            // damage klo ada poison
            if (poisonLength > 0) {
                hp--;
                poisonLength--;
            }

            // naikin walk count
            tilesWalkedWithSpeed = 0;
            walkCount++;
        }

        State newState = new State(
                state.position(),
                state.keyCount(),
                state.collectedGreenFlags(),
                walkCount,
                tilesWalkedWithSpeed,
                poisonLength,
                speedLength,
                state.collectedKeys(),
                state.collectedFlags(),
                state.collectedSpells(),
                state.brokenHoles(),
                state.ninjasPlayerInside(),
                state.wizardsPlayerInside(),
                state.fireMonstersPlayerInside()
        );

        hp = Math.clamp(hp, 0, 100);

        return new Result(newState, hp);
    }

    private static Result processTile(
            GameObject[][] maze,
            State state,
            int hp
    ) {
        int r = state.position().row();
        int c = state.position().col();
        Point currPos = new Point(r, c); // koordinat player sekarang ini

        // obstacle yang ada di koordinat ini
        GameObject obj = maze[r][c];

        int keyCount = state.keyCount(); // ngambil jumlah key yang dimiliki player
        Set<Point> collectedFlags = new HashSet<>(state.collectedFlags()); // flags mana saja yang sudah di ambil player
        Set<Point> collectedKeys = new HashSet<>(state.collectedKeys()); // key mana saja yang sudah di ambil player
        Set<Point> collectedSpells = new HashSet<>(state.collectedSpells()); // spell mana saja yang sudah di ambil player
        Set<Point> brokenHoles = new HashSet<>(state.brokenHoles()); // hole mana saja yang sudah di pijak player
        int greenFlagCount = state.collectedGreenFlags(); // jumlah flag ijo
        int fireLen = state.fireRemaining(); //
        int speedLen = state.speedRemaining();


        if (obj == null) {
            return new Result(state, hp);
        }

        // jika obstacle yg dipijak adalah:
        switch (obj) {
            case Spike spike -> hp -= 10;
            case Fire fire -> {

                // reset fire length
                // bakar player buat 10 step
                fireLen = 10;
            }
            case Hole hole -> {
                // damage player 5 hp lalu hole collision = true
                hp -= 5;
                brokenHoles.add(new Point(r, c));
            }
            case Key key -> {

                // collect only uncollected keys
                if (!collectedKeys.contains(currPos)) {
                    keyCount++;

                    // add key to the set of collected keys
                    collectedKeys.add(currPos);
                }
            }
            case FlagGreen flagG -> {

                // collect only uncollected flags
                if (!collectedFlags.contains(currPos)) {
                    greenFlagCount++;

                    // add flag to set of collected flags
                    collectedFlags.add(currPos);
                }
            }
            case FlagLocked flagLocked -> {

                // collect only uncollected flags dan jika punya key
                if (keyCount > 0 && !collectedFlags.contains(currPos)) {

                    // add flag to set of collected flags
                    collectedFlags.add(currPos);
                    greenFlagCount++;
                    keyCount--; // remove one key
                }
            }
            case FlagRed flagR -> {

                // collect the interacted flag without adding to greenFlagCount
                collectedFlags.add(currPos);
            }
            case HealSpell heal -> {

                // collect only uncollected spells
                if (!collectedSpells.contains(currPos)) {

                    // add spell to set of collected spells
                    collectedSpells.add(currPos);
                    hp += 20;
                }
            }
            case SpeedSpell speed -> {

                // collect only uncollected spells
                if (!collectedSpells.contains(currPos)) {

                    // add spell to set of collected spells
                    collectedSpells.add(currPos);
                    speedLen = 20;
                }
            }
            default -> {}
        }

        State newState = new State(
                currPos, // posisi
                keyCount, // jumlah key
                greenFlagCount, // jumlah flag ijo
                state.walkCount(), // jumlah player jalan
                state.tilesWalkedWithSpeed(), // jumlah player jalan dengan speed
                fireLen, // lama sisa api aktif
                speedLen, // lama sisa speed aktif
                collectedKeys, // list key yg udh di ambil
                collectedFlags, // list flag yg udh di ambil
                collectedSpells, // list spell yg udh di ambil
                brokenHoles, // list hole yg udh di pijak
                state.ninjasPlayerInside(),
                state.wizardsPlayerInside(),
                state.fireMonstersPlayerInside()
        );

        if (hp < 0) {
            hp = 0;
        } else if (hp > 100) {
            hp = 100;
        }

        return new Result(newState, hp);
    }

    private static boolean inBound(State state) {
        int r = state.position().row();
        int c = state.position().col();

        return r >= 0 && r < GamePanel.ROW_HEIGHT && c >= 0 && c < GamePanel.COL_WIDTH;
    }

    private static boolean isBlocked(GameObject[][] maze, State state) {
        int r = state.position().row();
        int c = state.position().col();

        if (r < 0 || r >= GamePanel.ROW_HEIGHT || c < 0 || c >= GamePanel.COL_WIDTH) {
            return true;
        }

        GameObject obj = maze[r][c];

        // handle all collision
        if (obj instanceof BushWall ||
                obj instanceof Ninja ||
                obj instanceof Wizard ||
                obj instanceof FireMonster ||
                obj instanceof Elf) {
            return true;
        }

        // handle Hole's dynamic collision
        // klo hole belom di pijak gk ada collision
        // klo UDAH di pijak ADA collision
        if (obj instanceof Hole) {
            return state.brokenHoles().contains(new Point(r, c));
        }

        // safely handle FireMonster's extended hitbox
        // jika di kiri player ada fire monster, maka ada collision
        int leftCol = c - 1;
        if (leftCol >= 0 && maze[r][leftCol] instanceof FireMonster) {
            return true;
        }

        return false;
    }
}
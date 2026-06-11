package io.github.maze.maze.solver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import io.github.maze.maze.Maze;
import io.github.maze.obstacles.BushWall;
import io.github.maze.obstacles.Elf;
import io.github.maze.obstacles.Fire;
import io.github.maze.obstacles.FireMonster;
import io.github.maze.obstacles.FlagGreen;
import io.github.maze.obstacles.FlagLocked;
import io.github.maze.obstacles.FlagRed;
import io.github.maze.obstacles.HealSpell;
import io.github.maze.obstacles.Hole;
import io.github.maze.obstacles.Key;
import io.github.maze.obstacles.Ninja;
import io.github.maze.obstacles.PoisonSpell;
import io.github.maze.obstacles.Portal;
import io.github.maze.obstacles.SpeedSpell;
import io.github.maze.obstacles.Spike;
import io.github.maze.obstacles.Wizard;
import io.github.maze.util.Point;

public class MazeSolver {

    private record Result(
            State state,
            int hp
    ) {}

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};
    private static final char[] DIR = {'U', 'D', 'L', 'R'};

    private static int totalFlag;
    private static String bestPath;
    public static StringBuilder backtrackingPath = new StringBuilder(3000000);
    public static int backtrackingLength = 0;
    private static int bestRemainingHp;

    private static Map<State, int[]> memo;

    public static String solve(GamePanel gp) {

        Point pos = new Point(gp.maze.player.getTileY(), gp.maze.player.getTileX());
        State initialState = initState(pos);

        totalFlag = gp.maze.flagCount;
        memo = new HashMap<>();
        bestRemainingHp = -1;
        bestPath = "";
        backtrackingLength = 0;
        backtrackingPath.setLength(0);

        StringBuilder path = new StringBuilder();

        for (int maxDepth = 20; maxDepth <= 2000; maxDepth += 20) {

            path.setLength(0);
            memo.clear();
            bestRemainingHp = -1;
            bestPath = "";

            initialState = initState(pos);

            backtracking(
                    gp.maze.obstacleMap,
                    initialState,
                    100,
                    path,
                    maxDepth
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
            int maxDepth
    ) {

        if (!bestPath.isEmpty() && bestRemainingHp == 100 && path.length() >= bestPath.length()) {
            return;
        }

        if (path.length() > maxDepth) {
            return;
        }

        if (path.length() % 1000 == 0 && path.length() > 0) {
            System.out.println("DEPTH = " + path.length());
        }

        if (path.length() % 1000 == 0) {
            System.out.println("DEPTH = " + path.length());
        }

        // check out of bound
        if (!inBound(state)) return;

        // check if player is able to move into that tile or not
        if (isBlocked(maze, state)) return;

        // handle tile effects:
        // spike, fire, hole, key, flagGreen, flagLocked, key, healSpell, poisonSpell, speedSpell
        Result tileResult = processTile(maze, state, hp);

        // update player:
        //
        // speedRemaining
        // speedParity
        // walkCount
        // poisonRemaining
        // poison damage
        Result playerUpdateResult = updateWalkCountAndStatusEffects(tileResult);
        state = playerUpdateResult.state;
        hp = playerUpdateResult.hp;

        // teleport player if there is a portal
        state = processPortal(maze, state);

        // apply elf healing
        hp = applyElfHealing(maze, state.position(), hp);

        // apply enemy damage
        // this updates: ninjaEntryWalkCount, wizardEntryWalkCount, fireMonstersTriggered
        Result enemyDamageResult = applyEnemyDamage(maze, state, hp);
        state = enemyDamageResult.state;
        hp = enemyDamageResult.hp;

        // check lose (lose base case)
        if (hp <= 0) {
            return;
        }


        if (memo.size() % 1000 == 0) {
            System.out.println(
                    "memo=" + memo.size()
            );
        }

        // remove unnecessary branches
        if (shouldPrune(state, hp, path.length())) return;

        // check win
        if (state.collectedGreenFlags() == totalFlag) {

            System.out.println(
                    state.position()
                            + " flagsSet=" + state.collectedFlags()
            );

            // one solution found. check if the
            // path is the current best path
            // if a tie, take the previous path
            if (
                    hp > bestRemainingHp
                            || (
                            hp == bestRemainingHp
                                    && (
                                    bestPath.isEmpty()
                                            || path.length() < bestPath.length()
                            )
                    )
            ) {
                bestRemainingHp = hp;
                bestPath = path.toString();
                backtrackingPath.setLength(0);
                backtrackingPath.append(bestPath);

                System.out.println(
                        "depth=" + path.length()
                                + " pos=" + state.position()
                                + " walk=" + state.walkCount()
                                + " flags=" + state.collectedGreenFlags()
                );
            }

            // win base case
            return;
        }

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
                state.poisonRemaining(),
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

            // add position to path taken
            path.append(DIR[i]);
            backtrackingPath.append(DIR[i]);

            // backtrack the next position
            backtracking(
                    maze,
                    nextState,
                    hp,
                    path,
                    maxDepth
            );
            path.deleteCharAt(path.length() - 1);
        }
    }

    private static boolean shouldPrune(
            State state,
            int hp,
            int pathLength
    ) {
        // if hp in memo is more than the current HP, don't prune
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
        Point position = state.position();
        int startRow = Math.max(position.row() - 2, 0);
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
                state.poisonRemaining(),
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

    private static State processPortal(
            GameObject[][] maze,
            State state
    ) {

        Point currentPosition = state.position();

        int r = currentPosition.row();
        int c = currentPosition.col();

        GameObject obj = maze[r][c];
        if (obj instanceof Portal origin) {

            int destR = (int) (origin.getConnection().getY() / GamePanel.TILE_SIZE);
            int destC = (int) (origin.getConnection().getX() / GamePanel.TILE_SIZE);

            return new State(
                    new Point(destR, destC),
                    state.keyCount(),
                    state.collectedGreenFlags(),
                    state.walkCount(),
                    state.tilesWalkedWithSpeed(),
                    state.poisonRemaining(),
                    state.speedRemaining(),
                    state.collectedKeys(),
                    state.collectedFlags(),
                    state.collectedSpells(),
                    state.brokenHoles(),
                    state.ninjasPlayerInside(),
                    state.wizardsPlayerInside(),
                    state.fireMonstersPlayerInside()
            );
        }

        return state;
    }

    // exactly teh same as update method in Player class
    private static Result updateWalkCountAndStatusEffects(
            Result result
    ) {
        State state = result.state;
        int hp = result.hp;

        int speedLength = state.speedRemaining();
        int poisonLength = state.poisonRemaining();
        int walkCount = state.walkCount();
        int tilesWalkedWithSpeed = state.tilesWalkedWithSpeed();

        if (speedLength > 0) {

            tilesWalkedWithSpeed++;

            if (tilesWalkedWithSpeed % 2 == 0) {

                if (poisonLength > 0) {
                    hp--;
                    poisonLength--;
                }

                walkCount++;
            }

            speedLength--;

            if (speedLength <= 0) {
                tilesWalkedWithSpeed = 0;
            }

        } else {

            if (poisonLength > 0) {
                hp--;
                poisonLength--;
            }

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
        Point currPos = new Point(r, c);

        GameObject obj = maze[r][c];

        int keyCount = state.keyCount();
        Set<Point> collectedFlags = new HashSet<>(state.collectedFlags());
        Set<Point> collectedKeys = new HashSet<>(state.collectedKeys());
        Set<Point> collectedSpells = new HashSet<>(state.collectedSpells());
        Set<Point> brokenHoles = new HashSet<>(state.brokenHoles());
        int greenFlagCount = state.collectedGreenFlags();
        int poisonLen = state.poisonRemaining();
        int speedLen = state.speedRemaining();


        if (obj == null) {
            return new Result(state, hp);
        }

        switch (obj) {
            case Spike spike -> hp -= 10;
            case Fire fire -> hp -= 5;
            case Hole hole -> {
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
            case FlagLocked flagL -> {

                // collect only uncollected flags
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
            case PoisonSpell poison -> {

                // collect only uncollected spells
                if (!collectedSpells.contains(currPos)) {

                    // add spell to set of collected spells
                    poisonLen = 10;
                    collectedSpells.add(currPos);
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
                currPos,
                keyCount,
                greenFlagCount,
                state.walkCount(),
                state.tilesWalkedWithSpeed(),
                poisonLen,
                speedLen,
                collectedKeys,
                collectedFlags,
                collectedSpells,
                brokenHoles,
                state.ninjasPlayerInside(),
                state.wizardsPlayerInside(),
                state.fireMonstersPlayerInside()
        );

        hp = Math.clamp(hp, 0, 100);

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
        if (obj instanceof Hole) {
            return state.brokenHoles().contains(new Point(r, c));
        }

        // safely handle FireMonster's extended hitbox
        int leftCol = c - 1;
        if (leftCol >= 0) {
            if (maze[r][leftCol] instanceof FireMonster) {
                return true;
            }
        }

        return false;
    }
}
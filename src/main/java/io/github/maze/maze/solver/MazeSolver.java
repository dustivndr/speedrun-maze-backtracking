package io.github.maze.maze.solver;

import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.maze.*;
import io.github.maze.obstacles.*;
import io.github.maze.util.*;

import java.util.*;

public class MazeSolver {

    private record Result(
            State state,
            int hp
    ) {}

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};
    private static final char[] DIR = {'U', 'D', 'L', 'R'};

    private static String bestPath;
    private static int bestRemainingHp;

    private static int totalFlags;

    private static Map<Point, Portal> portalLookup;

    private static Map<State, Integer> memo;

    public static String solve(Maze maze) {

        Point pos = new Point(maze.player.getTileY(), maze.player.getTileX());
        State initialState = initState(pos);

        totalFlags = maze.flagCount;
        portalLookup = buildPortals(maze.obstacleMap);
        memo = new HashMap<>();

        StringBuilder path = new StringBuilder();

        backtracking(
                maze.obstacleMap,
                initialState,
                100,
                path
        );

        return path.toString();
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
                new HashMap<>(),
                new HashMap<>(),
                new HashSet<>()
        );
    }

    private static void backtracking(
            GameObject[][] maze,
            State state,
            int hp,
            StringBuilder path
    ) {
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

        // remove unnecessary branches
        shouldPrune(state, hp);

        for (int i = 0; i < 4; i++) {

            // get current position
            Point curr = state.position();

            // get next position
            Point nextPos = new Point(curr.row() + DR[i], curr.col() + DR[i]);

            // add position to path taken
            path.append(DIR[i]);

            // backtrack on the next position
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
                    state.brokenHoles(),
                    state.ninjasPlayerInside(),
                    state.wizardsPlayerInside(),
                    state.fireMonstersPlayerInside()
            );

            // backtrack
            path.deleteCharAt(path.length() - 1);

        }

    }

    private static boolean shouldPrune(
            State state,
            int hp
    ) {
        // if hp in memo is more than the current HP, don't prune
        if (memo.containsKey(state) && memo.get(state) >= hp) {
            return true;
        }

        // else prune
        memo.put(state, hp);
        return false;
    }

    private static Result applyEnemyDamage(
            GameObject[][] maze,
            State state,
            int hp
    ) {
        Point position = state.position();
        int startRow = Math.max(position.row() - 2, 0);
        int startCol = Math.max(position.col() - 2, 0);
        int endRow = Math.min(startRow + 4, GamePanel.ROW_HEIGHT);
        int endCol = Math.min(startCol + 5, GamePanel.COL_WIDTH);

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

                // check current position for FireMOnster
                if (obj instanceof FireMonster fm) {

                    Point pos = new Point(r, c);
                    fmsInside.add(pos);

                    // if player isn't already inside the FireMonster, attack
                    if (!fireMonstersPlayerInside.contains(pos)) {
                        hp -= 20;
                    }
                }

                // check current position for Wizard
                if (
                        obj instanceof Wizard w &&

                        // range of wizard
                        r >= startRow && r <= endRow &&
                        c >= startCol && c <= endCol - 1
                ) {
                    Point pos = new Point(r, c);
                    int moveCountInRange = 0;

                    wsInside.add(pos);

                    // player has just entered wizard range
                    if (!wizardsPlayerInside.containsKey(pos)) {
                        wizardsPlayerInside.put(pos, 0);
                        hp -= 10;
                    }

                    // player was already within wizard range
                    else {

                        // check for tile moves
                        moveCountInRange = wizardsPlayerInside.get(pos);

                        // move count in wizard range is a multiple of 4
                        if (moveCountInRange % 4 == 0) {
                            hp -= 10;
                        }

                    }

                    // add 1 to move count in that wizard's range
                    wizardsPlayerInside.put(pos, moveCountInRange + 1);
                }

                // check current position for Ninja
                if (
                        obj instanceof Ninja &&

                        // check if player is in Ninja range
                        r >= position.row() - 1 && r <= position.row() + 1 &&
                        c >= position.col() - 1 && c <= position.col() + 1
                ) {
                    Point pos = new Point(r, c);
                    int moveCountInRange = 0;

                    nsInside.add(pos);

                    // player has just entered Ninja range
                    if (!ninjasPlayerInside.containsKey(pos)) {
                        ninjasPlayerInside.put(pos, 0);
                        hp -= 5;
                    }

                    // player was already in NInja range
                    else {

                        // check for tile move
                        moveCountInRange = ninjasPlayerInside.get(pos);

                        // move count is a multiple of 2
                        if (moveCountInRange % 2 == 0) {
                            hp -= 5;
                        }

                    }

                    // add 1 to that ninja's player move count in range
                    ninjasPlayerInside.put(pos, moveCountInRange + 1);
                }
            }
        }

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
        if (obj instanceof Portal) {

            int destR = (int) (obj.getY() / GamePanel.TILE_SIZE);
            int destC = (int) (obj.getX() / GamePanel.TILE_SIZE);

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
                state.brokenHoles(),
                state.ninjasPlayerInside(),
                state.wizardsPlayerInside(),
                state.fireMonstersPlayerInside()
        );

        return new Result(newState, Math.max(hp, 0));
    }

    private static Result processTile(
            GameObject[][] maze,
            State state,
            int hp
    ) {
        int r = state.position().row();
        int c = state.position().col();

        GameObject obj = maze[r][c];
        int keyCount = state.keyCount();
        Set<Point> collectedFlags = new HashSet<>(state.collectedFlags());
        Set<Point> collectedKeys = new HashSet<>(state.collectedKeys());
        Set<Point> brokenHoles = new HashSet<>(state.brokenHoles());
        int flagCount = state.collectedGreenFlags();
        int poisonLen = state.poisonRemaining();
        int speedLen = state.speedRemaining();

        switch (obj) {
            case Spike spike -> hp -= 5;
            case Fire fire -> hp -= 5;
            case Hole hole -> hp -= 5;
            case Key key -> {
                keyCount++;
                collectedKeys.add(new Point(r, c));
            }
            case FlagGreen flagG -> {
                collectedFlags.add(new Point(r, c));
                flagCount++;
            }
            case FlagLocked flagL -> {
                if (keyCount > 0) {
                    collectedFlags.add(new Point(r, c));
                    flagCount++;
                }
            }
            case HealSpell heal -> hp += 20;
            case PoisonSpell poison -> poisonLen += 10;
            case SpeedSpell speed -> speedLen += 20;
            default -> {}
        }

        State newState = new State(
                new Point(state.position()),
                keyCount,
                flagCount,
                state.walkCount(),
                state.tilesWalkedWithSpeed(),
                poisonLen,
                speedLen,
                collectedKeys,
                collectedFlags,
                brokenHoles,
                new HashMap<>(state.ninjasPlayerInside()),
                new HashMap<>(state.wizardsPlayerInside()),
                new HashSet<>(state.fireMonstersPlayerInside())
        );

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

    private static Map<Point, Portal> buildPortals(GameObject[][] maze) {
        Map<Point, Portal> map = new HashMap<>();

        for (int i = 0; i < GamePanel.ROW_HEIGHT; i++) {
            for (int j = 0; j < GamePanel.COL_WIDTH; j++) {
                if (maze[i][j] instanceof Portal portalObject) {
                    map.put(
                            new Point(i, j),
                            portalObject
                    );
                }
            }
        }

        return map;
    }
}

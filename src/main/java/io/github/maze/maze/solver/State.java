package io.github.maze.maze.solver;

import io.github.maze.util.Point;

import java.util.*;

public final class State {

    // POSITION

    private final Point position;

    // PLAYER

    private final int keyCount;
    private final int collectedGreenFlags;
    private final int tilesWalkedWithSpeed;

    // TIMERS

    private final int walkCount;
    private final int poisonRemaining;

    private final int speedRemaining;

    // WORLD STATE

    private final Set<Point> collectedKeys;

    private final Set<Point> collectedFlags;

    private final Set<Point> brokenHoles;

    // ENEMY STATE

    /*
     * Ninja:
     * attack on entry
     * then every 2 walkCount
     */
    private final Map<Point, Integer> ninjaEntryWalkCount;

    /*
     * Wizard:
     * attack on entry
     * then every 4 walkCount
     */
    private final Map<Point, Integer> wizardEntryWalkCount;

    /*
     * FireMonster:
     * attack on entry only
     */
    private final Set<Point> fireMonstersAlreadyTriggered;

    public State(
            Point position,
            int keyCount,
            int collectedGreenFlags,
            int walkCount,
            int tilesWalkedWithSpeed,
            int poisonRemaining,
            int speedRemaining,
            Set<Point> collectedKeys,
            Set<Point> collectedFlags,
            Set<Point> brokenHoles,
            Map<Point, Integer> ninjaEntryWalkCount,
            Map<Point, Integer> wizardEntryWalkCount,
            Set<Point> fireMonstersAlreadyTriggered
    ) {

        this.position = position;

        this.keyCount = keyCount;
        this.collectedGreenFlags = collectedGreenFlags;

        this.walkCount = walkCount;
        this.tilesWalkedWithSpeed = tilesWalkedWithSpeed;

        this.poisonRemaining = poisonRemaining;
        this.speedRemaining = speedRemaining;

        this.collectedKeys =
                Collections.unmodifiableSet(
                        new HashSet<>(collectedKeys));

        this.collectedFlags =
                Collections.unmodifiableSet(
                        new HashSet<>(collectedFlags));

        this.brokenHoles =
                Collections.unmodifiableSet(
                        new HashSet<>(brokenHoles));

        this.ninjaEntryWalkCount =
                Collections.unmodifiableMap(
                        new HashMap<>(ninjaEntryWalkCount));

        this.wizardEntryWalkCount =
                Collections.unmodifiableMap(
                        new HashMap<>(wizardEntryWalkCount));

        this.fireMonstersAlreadyTriggered =
                Collections.unmodifiableSet(
                        new HashSet<>(fireMonstersAlreadyTriggered));
    }

    // GETTERS

    public int tilesWalkedWithSpeed() { return tilesWalkedWithSpeed; }
    public Point position() { return position; }
    public int keyCount() { return keyCount; }
    public int collectedGreenFlags() { return collectedGreenFlags; }
    public int walkCount() { return walkCount; }
    public int poisonRemaining() { return poisonRemaining; }
    public int speedRemaining() { return speedRemaining; }
    public Set<Point> collectedKeys() { return collectedKeys; }
    public Set<Point> collectedFlags() { return collectedFlags; }
    public Set<Point> brokenHoles() { return brokenHoles; }
    public Map<Point, Integer> ninjasPlayerInside() { return ninjaEntryWalkCount; }
    public Map<Point, Integer> wizardsPlayerInside() { return wizardEntryWalkCount; }
    public Set<Point> fireMonstersPlayerInside() { return fireMonstersAlreadyTriggered; }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof State other)) {
            return false;
        }

        return keyCount == other.keyCount
                && collectedGreenFlags == other.collectedGreenFlags
                && walkCount == other.walkCount
                && poisonRemaining == other.poisonRemaining
                && speedRemaining == other.speedRemaining
                && Objects.equals(position, other.position)
                && Objects.equals(collectedKeys, other.collectedKeys)
                && Objects.equals(collectedFlags, other.collectedFlags)
                && Objects.equals(brokenHoles, other.brokenHoles)
                && Objects.equals(ninjaEntryWalkCount, other.ninjaEntryWalkCount)
                && Objects.equals(wizardEntryWalkCount, other.wizardEntryWalkCount)
                && Objects.equals(fireMonstersAlreadyTriggered,
                other.fireMonstersAlreadyTriggered);
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                position,
                keyCount,
                collectedGreenFlags,
                walkCount,
                poisonRemaining,
                speedRemaining,
                collectedKeys,
                collectedFlags,
                brokenHoles,
                ninjaEntryWalkCount,
                wizardEntryWalkCount,
                fireMonstersAlreadyTriggered
        );
    }
}
package io.github.maze.entities;

public class Player {

    private int health;

    public void damage(int i) {
        health -= i;

        if (health <= 0)
            health = 0;
    }

//    public int getTileX() {
//    }
//
//    public int getTileY() {
//    }
}

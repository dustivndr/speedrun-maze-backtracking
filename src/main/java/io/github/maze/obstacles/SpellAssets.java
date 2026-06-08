package io.github.maze.obstacles;

import io.github.maze.util.Util;
import javafx.scene.image.Image;

public class SpellAssets {

    private Image[] frames = new Image[3];

    public static final int HEAL = 0;
    public static final int POISON = 1;
    public static final int SPEED = 2;

    public SpellAssets() {
        frames[0] = Util.getScaledImage("/image/spell/HEAL SPELL.png");
        frames[1] = Util.getScaledImage("/image/spell/POISON SPELL.png");
        frames[2] = Util.getScaledImage("/image/spell/SPEED SPELL.png");
    }

    public Image getSpell(int type) {
        return frames[type];
    }
}

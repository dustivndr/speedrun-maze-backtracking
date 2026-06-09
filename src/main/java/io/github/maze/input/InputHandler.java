package io.github.maze.input;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class InputHandler {

    private final Set<KeyCode> pressedKeys =
            new HashSet<>();

    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getCode());
    }

    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getCode());
    }

    public boolean isPressed(KeyCode key) {
        return pressedKeys.contains(key);
    }

    public void press(KeyCode key) {
        pressedKeys.add(key);
    }
    
    public void release(KeyCode key) {
        pressedKeys.remove(key);
    }

    public void releaseAll() {
        pressedKeys.clear();
    }

}

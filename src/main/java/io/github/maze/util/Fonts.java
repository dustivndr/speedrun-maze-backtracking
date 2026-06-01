package io.github.maze.util;

/*
* FONT MANAGER
* Already working! DO NOT TOUCH !!
*
*/

import javafx.scene.text.Font;

public final class Fonts {

    private static final Font BASE = Font.loadFont(
            Fonts.class.getResourceAsStream(
                    "/fonts/Jersey10-Regular.ttf"
            ),
            12
    );

    public static final String FAMILY = BASE.getFamily();

    public static void initialize() {
        // intentionally empty
        // calling this forces class initialization
    }

}

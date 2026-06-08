package io.github.maze.audio;

/*
* CONTROL SOUND EFFECT IN THE GAME
*
* example on how to use in game:
* SoundManager.CLICK.play();
*
*/

import javafx.scene.media.AudioClip;

public final class SoundManager {

//    public static final AudioClip CLICK;
//    public static final AudioClip SPIKE;
//    public static final AudioClip WIN;
//
//    static {
//
//        CLICK = load("/audio/click.wav");
//        SPIKE = load("/audio/spike.wav");
//        WIN = load("/audio/win.wav");
//
//    }

    private static AudioClip load(String path) {

        return new AudioClip(
                SoundManager.class
                        .getResource(path)
                        .toExternalForm()
        );
    }

}

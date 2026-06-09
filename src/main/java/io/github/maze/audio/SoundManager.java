package io.github.maze.audio;

/*
* CONTROL SOUND EFFECT IN THE GAME
*
* example on how to use in game:
* SoundManager.<SOUND-NAME>.play();
*
*/

import javafx.scene.media.AudioClip;

public final class SoundManager {

    public static final AudioClip ELF_VOICE;
    public static final AudioClip FAKE_FLAG_SFX;
    public static final AudioClip FIRE_MONSTER_VOICE;
    public static final AudioClip FIRE_SFX;
    public static final AudioClip FOOTSTEP_SFX;
    public static final AudioClip GREEN_FLAG_SFX;
    public static final AudioClip HEAL_SPELL_SFX;
    public static final AudioClip KUNAI_SFX;
    public static final AudioClip METEOR_SFX;
    public static final AudioClip PLAYER_FALL_SFX;
    public static final AudioClip POISON_SPELL;
    public static final AudioClip PORTAL_SFX;
    public static final AudioClip SPEED_SPELL_SFX;
    public static final AudioClip SPIKE_HIT_SFX;
    public static final AudioClip SPIKE_SFX;
    public static final AudioClip THUNDER_SFX;
    public static final AudioClip WIZARD_VOICE_1;
    public static final AudioClip WIZARD_VOICE_2;

    static {

        ELF_VOICE = load("/audio/sfx/elf-voice.mp3");
        FAKE_FLAG_SFX = load("/audio/sfx/fake-flag-sfx.mp3");
        FIRE_MONSTER_VOICE = load("/audio/sfx/fire-monster-voice.mp3");
        FIRE_SFX = load("/audio/sfx/fire-sfx.mp3");
        FOOTSTEP_SFX = load("/audio/sfx/footstep-sfx.mp3");
        GREEN_FLAG_SFX = load("/audio/sfx/green-flag-sfx.mp3");
        HEAL_SPELL_SFX = load("/audio/sfx/heal-spell-sfx.mp3");
        KUNAI_SFX = load("/audio/sfx/kunai-sfx.mp3");
        METEOR_SFX = load("/audio/sfx/meteor-sfx.mp3");
        PLAYER_FALL_SFX = load("/audio/sfx/player-fall-voice.mp3");
        POISON_SPELL = load("/audio/sfx/poison-spell.mp3");
        PORTAL_SFX = load("/audio/sfx/portal-sfx.mp3");
        SPEED_SPELL_SFX = load("/audio/sfx/speed-spell-sfx.mp3");
        SPIKE_HIT_SFX = load("/audio/sfx/spike-hit-sfx.mp3");
        SPIKE_SFX = load("/audio/sfx/spike-sfx.mp3");
        THUNDER_SFX = load("/audio/sfx/thunder-sfx.mp3");
        WIZARD_VOICE_1 = load("/audio/sfx/wizard-voice-1.mp3");
        WIZARD_VOICE_2 = load("/audio/sfx/wizard-voice-2.mp3");

    }

    private static AudioClip load(String path) {

        return new AudioClip(
                SoundManager.class
                        .getResource(path)
                        .toExternalForm()
        );
    }

}

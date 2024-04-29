package controller.resources;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class Audio {

    private static final String MUSIC_PATH = "src/main/resources/audio/music/";
    private static final String SOUND_PATH = "src/main/resources/audio/sound/";

    private static Clip music;
    private static Clip sound;
    private static AudioInputStream audioMusic;
    private static AudioInputStream audioSound;
    private static float musicVolume = 1.0f;
    private static float soundVolume = 1.0f;

    public static void playMusic(final String musicName) {
        try {
            audioMusic = AudioSystem.getAudioInputStream(new File(MUSIC_PATH + musicName));
            music = AudioSystem.getClip();
            music.open(audioMusic);
            music.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void stopMusic() {
        music.close();
    }

    public static void playSound(final String soundName) {
        try {
            audioSound = AudioSystem.getAudioInputStream(new File(SOUND_PATH + soundName));
            sound = AudioSystem.getClip();
            sound.open(audioSound);
            sound.start();
            if (sound != null) {
                FloatControl gainControl = (FloatControl) sound.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(20f * (float) Math.log10(soundVolume));
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static Clip getMusic() {
        return music;
    }

    public static Clip getSound() {
        return sound;
    }

    public static void setSoundVolume(final float volume) {
        soundVolume = volume;
    }

    public static float getSoundVolume() {
        return soundVolume;
    }

    public static int getSoundSliderValue() {
        return (int) soundVolume * 50;
    }

    public static void setMusicVolume(final float volume) {
        musicVolume = volume;
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static int getMusicSliderValue() {
        return (int) musicVolume * 50;
    }
}

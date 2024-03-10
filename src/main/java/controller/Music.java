package controller;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import view.SwingFactory;

public class Music {

    private static Clip music;
    private static Clip song;
    private static AudioInputStream audioMusic;
    private static AudioInputStream audioSong;
    private static float musicVolume = 1.0f;
    private static float songVolume = 1.0f;
    private static int musicSlider = 50;
    private static int songSlider = 50;

    public static void playMusic(final String musicPath) {
        try {
            audioMusic = AudioSystem.getAudioInputStream(new File(SwingFactory.getPath() + musicPath));
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

    public static void playSong(final String songPath) {
        try {
            audioSong = AudioSystem.getAudioInputStream(new File(SwingFactory.getPath() + songPath));
            song = AudioSystem.getClip();
            song.open(audioSong);
            song.start();
            if (Music.getSong() != null) {
                FloatControl gainControl = (FloatControl) Music.getSong().getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(20f * (float) Math.log10(Music.getSongVolume()));
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

    public static Clip getSong() {
        return song;
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static float getSongVolume() {
        return songVolume;
    }

    public static void setMusicVolume(final float musicVolume) {
        Music.musicVolume = musicVolume;
    }

    public static void setSongVolume(final float songVolume) {
        Music.songVolume = songVolume;
    }

    public static int getMusicSlider() {
        return musicSlider;
    }

    public static int getSongSlider() {
        return songSlider;
    }

    public static void setMusicSlider(final int musicSlider) {
        Music.musicSlider = musicSlider;
    }

    public static void setSongSlider(final int songSlider) {
        Music.songSlider = songSlider;
    }
}

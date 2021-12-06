package uet.oop.bomberman.graphics;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[10];
    long time = 0;
    //private static Sound sound;

    public Sound() {
        soundURL[0] = getClass().getResource("/sounds/bomberGo.wav");
        soundURL[1] = getClass().getResource("/sounds/getExtraItems.wav");
        soundURL[2] = getClass().getResource("/sounds/passLevel.wav");
        soundURL[3] = getClass().getResource("/sounds/putBomb.wav");
        soundURL[4] = getClass().getResource("/sounds/BombExplode.wav");
        soundURL[5] = getClass().getResource("/sounds/death.wav");
        soundURL[6] = getClass().getResource("/sounds/background.wav");
    }
/*
    public static Sound getInstance() {
        if (sound == null) sound = new Sound();
        return sound;
    }
*/

    public void setFile(int i) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(){

        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public long getLengthFile(int i) {
        return soundURL[i].getFile().length();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void close() {
        clip.close();
    }

    public void reset() {

        clip.setMicrosecondPosition(0);
    }

    public void stop() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }



    public float getDuration(int i) {
        float durationInSeconds = 0;
        try {
            File file = new File(soundURL[i].getFile());
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long audioFileLength = file.length();
            int frameSize = format.getFrameSize();
            float frameRate = format.getFrameRate();
            durationInSeconds = (audioFileLength / (frameSize * frameRate));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return durationInSeconds;
    }

    /*public static void main(String[] args) throws Exception {
        Sound sound = Sound.getInstance();

    }*/

}

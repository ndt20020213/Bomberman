package uet.oop.bomberman.sound;

import java.util.HashMap;

public class GameSound {

    private final HashMap<String, Sound> sounds = new HashMap<>();

    private boolean soundStatus = true;

    public void changeSoundStatus() {
        if (soundStatus) {
            soundStatus = false;
            sounds.forEach((name, sound) -> {
                if (!name.equals("background")) sound.stop();
            });
        } else {
            soundStatus = true;
        }
    }

    public GameSound() {
        // bomberGo
        Sound bomberGo = new Sound();
        bomberGo.setFile(0);
        sounds.put("bomberGo", bomberGo);
        // getExtraItems
        Sound getExtraItems = new Sound();
        getExtraItems.setFile(1);
        sounds.put("getExtraItems", getExtraItems);
        // passLevel
        Sound passLevel = new Sound();
        passLevel.setFile(2);
        sounds.put("passLevel", passLevel);
        // putBomb
        Sound putBomb = new Sound();
        putBomb.setFile(3);
        sounds.put("putBomb", putBomb);
        // BombExplode
        Sound BombExplode = new Sound();
        BombExplode.setFile(4);
        sounds.put("BombExplode", BombExplode);
        // death
        Sound death = new Sound();
        death.setFile(5);
        sounds.put("death", death);
        // background
        Sound background = new Sound();
        background.setFile(6);
        sounds.put("background", background);
    }

    public void playPutBombSound() {
        if (!soundStatus) return;
        sounds.get("putBomb").play();
    }

    public void playGetExtraItemsSound() {
        if (!soundStatus) return;
        sounds.get("getExtraItems").play();
    }

    public void playBombExplodeSound() {
        if (!soundStatus) return;
        sounds.get("BombExplode").play();
    }

    private boolean isBomberGoSoundPlaying = false;

    public void playBomberGoSound() {
        if (!soundStatus) return;
        if (!isBomberGoSoundPlaying) {
            Sound sound = sounds.get("bomberGo");
            sound.play();
            sound.loop();
            isBomberGoSoundPlaying = true;
        }
    }

    public void stopBomberGoSound() {
        isBomberGoSoundPlaying = false;
        sounds.get("bomberGo").stop();
    }

    public void playWinSound() {
        if (!soundStatus) return;
        sounds.get("passLevel").play();
    }

    public void playLoseSound() {
        if (!soundStatus) return;
        sounds.get("death").play();
    }

    public void playBGM() {
        if (!soundStatus) return;
        Sound sound = sounds.get("background");
        if (sound != null) {
            sound.play();
            sound.loop();
        }
    }

    public void stopBGM() {
        Sound sound = sounds.get("background");
        if (sound != null) sound.stop();
    }
}

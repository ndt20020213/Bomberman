package uet.oop.bomberman.entities.player;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sound;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.entities.attack.Bomb;
import uet.oop.bomberman.entities.attack.effects.canDestroy;
import uet.oop.bomberman.entities.background.*;
import uet.oop.bomberman.entities.player.effects.*;
import uet.oop.bomberman.entities.player.properties.*;
import uet.oop.bomberman.structure.*;

public class Bomber extends Entity implements canDestroy,
        SpeedProperty, BombProperty, FlameProperty, HealthProperty,
        WallPassEffect, BombPassEffect, FlamePassEffect {

    private String name;

    public Bomber(int x, int y) {
        super(x, y, Sprite.player_down.getFxImage());
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(img, position.x + 4, position.y);
    }

    // Di chuyển và trạng thái.
    private long oldTime = 0;
    private char status = ' ';

    public boolean playing = false;

    public Sound sound = Sound.getInstance();


    @Override
    public void update() {
        long time = world.time; //tg hiện tại
        Point oldPosition = new Point(position.x, position.y);
        Rect oldRect = getRect();

        // tg hiện tại >= tg lần trước gần nhất gọi âm thanh + độ dài âm thanh thì mới gọi tiếp


        switch (status) {
            case 'W':
                position.y -= speed * (time - oldTime) / 1e9;
                if (playing && time > oldTime + 1e8/5) {
                    sound.setFile(0);
                    sound.play();
                }
                break;
            case 'S':
                position.y += speed * (time - oldTime) / 1e9;
                if (playing && time > oldTime + 1e8/5) {
                    sound.setFile(0);
                    sound.play();
                }
                break;
            case 'A':
                position.x -= speed * (time - oldTime) / 1e9;
                if (playing && time > oldTime + 1e8/5) {
                    sound.setFile(0);
                    sound.play();
                }
                break;
            case 'D':
                position.x += speed * (time - oldTime) / 1e9;
                if (playing && time > oldTime + 1e8/5) {
                    sound.setFile(0);
                    sound.play();
                }
                break;
        }
        oldTime = time;

        Rect newRect = getRect();
        if (!newRect.equals(oldRect)) {
            if (!checkWallPass(oldRect, newRect)) position = oldPosition;
            if (!checkBombPass(oldRect, newRect)) position = oldPosition;
        }
    }

    @Override
    public Cell getUnit() {
        Point position = getPosition();
        position.x += 6;
        position.y += 5;
        return new Cell(position);
    }

    @Override
    public Point getPosition() {
        return new Point(position.x + 8, position.y + 18);
    }

    @Override
    public Rect getRect() {
        Point point = getPosition();
        return new Rect(point.x, point.y, 12, 10);
    }

    public void keyPressed(String key) {
        switch (key) {
            case "W":
            case "Up":
                status = 'W';
                img = Sprite.player_up.getFxImage();
                playing = true;
                break;
            case "S":
            case "Down":
                status = 'S';
                img = Sprite.player_down.getFxImage();
                playing = true;
                break;
            case "A":
            case "Left":
                status = 'A';
                img = Sprite.player_left.getFxImage();
                playing = true;
                break;
            case "D":
            case "Right":
                status = 'D';
                img = Sprite.player_right.getFxImage();
                playing = true;
                break;
            case "Space":
                putBomb();
                sound.setFile(3);
                sound.play();
                break;
        }
    }

    public void keyReleased(String key) {
        if (key == null) {
            status = ' ';
            return;
        }
        switch (key) {
            case "W":
            case "Up":
                key = "W";
                playing = false;
                break;
            case "S":
            case "Down":
                key = "S";
                playing = false;
                break;
            case "A":
            case "Left":
                key = "A";
                playing = false;
                break;
            case "D":
            case "Right":
                key = "D";
                playing = false;
                break;
        }
        if (status == key.charAt(0)) status = ' ';
    }

    public void putBomb() {
        if (this.bomb <= 0 || health <= 0) return;
        Bomb bomb = new Bomb(this, flame);
        for (Bomb bomb1 : world.bombs) if (bomb.impact(bomb1)) return;
        world.entities.add(bomb);
    }

    //HealthProperty
    private int health = 3;

    @Override
    public boolean addHealth(int health) {
        this.health += health;
        return true;
    }

    //canDestroy
    @Override
    public void destroy() {
        if (checkFlamePass()) health--;
        if (health <= 0) world.removeEntity(this);
    }

    //SpeedProperty
    private int speed = 150;

    @Override
    public boolean addSpeed(int speed) {
        this.speed += speed;
        return true;
    }

    //BombProperty
    private int bomb = 1;

    @Override
    public boolean addBomb(int bomb) {
        this.bomb += bomb;
        return true;
    }

    //FlameProperty
    private int flame = 1;

    @Override
    public boolean addFlame(int flame) {
        this.flame += flame;
        return true;
    }

    //WallPassEffect
    private long wallPassTime = 0;

    @Override
    public boolean addWallPass(long time) {
        wallPassTime = world.time + time;
        return true;
    }

    @Override
    public boolean checkWallPass(Rect oldRect, Rect newRect) {
        if (world.time < wallPassTime) return true;
        for (Wall wall : world.walls)
            if (!wall.getRect().impact(oldRect) && wall.getRect().impact(newRect)) return false;
        for (Brick brick : world.bricks)
            if (!brick.getRect().impact(oldRect) && brick.getRect().impact(newRect)) return false;
        return true;
    }

    //BombPassEffect
    private long bombPassTime = 0;

    @Override
    public boolean addBombPass(long time) {
        bombPassTime = world.time + time;
        return true;
    }

    @Override
    public boolean checkBombPass(Rect oldRect, Rect newRect) {
        if (world.time < bombPassTime) return true;
        for (Bomb bomb : world.bombs)
            if (!bomb.getRect().impact(oldRect) && bomb.getRect().impact(newRect)) return false;
        return true;
    }

    //FlamePassEffect
    private long flamePassTime = 0;

    @Override
    public boolean addFlamePass(long time) {
        flamePassTime = world.time + time;
        return true;
    }

    @Override
    public boolean checkFlamePass() {
        return world.time >= flamePassTime;
    }
}

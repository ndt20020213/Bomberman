package uet.oop.bomberman.entities.player;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.Bomb;
import uet.oop.bomberman.entities.attack.effects.canDestroy;
import uet.oop.bomberman.entities.background.Wall;
import uet.oop.bomberman.entities.bricks.Brick;
import uet.oop.bomberman.entities.player.effects.BombPassEffect;
import uet.oop.bomberman.entities.player.effects.FlamePassEffect;
import uet.oop.bomberman.entities.player.effects.WallPassEffect;
import uet.oop.bomberman.entities.player.properties.BombProperty;
import uet.oop.bomberman.entities.player.properties.FlameProperty;
import uet.oop.bomberman.entities.player.properties.SpeedProperty;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Rect;

public class Bomber extends Entity implements canDestroy,
        SpeedProperty, BombProperty, FlameProperty,
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
        gc.drawImage(img, position.x, position.y - 10);
    }

    // Di chuyển và trạng thái.
    private long oldTime = 0;
    private char status = ' ';

    @Override
    public void update() {
        long time = BombermanGame.now;
        Rect newPosition = getRect();
        switch (status) {
            case 'W':
                newPosition.point.y -= speed * (time - oldTime) / 1e9;
                break;
            case 'S':
                newPosition.point.y += speed * (time - oldTime) / 1e9;
                break;
            case 'A':
                newPosition.point.x -= speed * (time - oldTime) / 1e9;
                break;
            case 'D':
                newPosition.point.x += speed * (time - oldTime) / 1e9;
                break;
        }
        oldTime = time;
        if (!newPosition.equals(getRect())){
            if (!checkWallPass(newPosition)) return;
            if (!checkBombPass(newPosition)) return;
        }
        position = newPosition.point;
    }

    public void keyPressed(String key) {
        switch (key) {
            case "W":
            case "Up":
                status = 'W';
                img = Sprite.player_up.getFxImage();
                break;
            case "S":
            case "Down":
                status = 'S';
                img = Sprite.player_down.getFxImage();
                break;
            case "A":
            case "Left":
                status = 'A';
                img = Sprite.player_left.getFxImage();
                break;
            case "D":
            case "Right":
                status = 'D';
                img = Sprite.player_right.getFxImage();
                break;
            case "Space":
                putBomb();
                break;
        }
    }

    public void keyReleased(String key) {
        if (key == null) status = ' ';
        else if (status == key.charAt(0)) status = ' ';
    }

    public void putBomb() {
        world.entities.add(new Bomb(position.getX(), position.getY(), this, flame));
    }

    //canDestroy
    private int health = 1;

    @Override
    public void destroy() {
        if (checkFlamePass(health, health-1)) return;
        health--;
    }

    //SpeedProperty
    private int speed = 200;

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
        wallPassTime = BombermanGame.now + time;
        return true;
    }

    @Override
    public boolean checkWallPass(Rect newPosition) {
        if (BombermanGame.now < wallPassTime) return true;
        Rect rect = new Rect(newPosition.point.x + 5, newPosition.point.y + 5, newPosition.width - 10, newPosition.height - 10);
        for (Wall wall : world.walls)
            if (wall.getRect().impact(rect)) return false;
        for (Brick brick : world.bricks)
            if (brick.getRect().impact(rect)) return false;
        return true;
    }

    //BombPassEffect
    private long bombPassTime = 0;

    @Override
    public boolean addBombPass(long time) {
        bombPassTime = BombermanGame.now + time;
        return true;
    }

    @Override
    public boolean checkBombPass(Rect newPosition) {
        if (BombermanGame.now < bombPassTime) return true;
        Rect rect = new Rect(newPosition.point.x + 5, newPosition.point.y + 5, newPosition.width - 10, newPosition.height - 10);
        for (Bomb bomb : world.bombs)
            if (bomb.getRect().impact(rect)) return false;
        return true;
    }

    //FlamePassEffect
    private long flamePassTime = 0;

    @Override
    public boolean addFlamePass(long time) {
        flamePassTime = BombermanGame.now + time;
        return true;
    }

    @Override
    public boolean checkFlamePass(int health, int newHealth) {
        return BombermanGame.now >= flamePassTime;
    }
}

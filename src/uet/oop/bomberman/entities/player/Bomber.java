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
import uet.oop.bomberman.structure.Point;
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
        gc.drawImage(img, position.x + 4, position.y);
    }

    // Di chuyển và trạng thái.
    private long oldTime = 0;
    private char status = ' ';

    @Override
    public void update() {
        long time = BombermanGame.now;
        Point point = new Point(position.x, position.y);
        switch (status) {
            case 'W':
                point.y -= speed * (time - oldTime) / 1e9;
                break;
            case 'S':
                point.y += speed * (time - oldTime) / 1e9;
                break;
            case 'A':
                point.x -= speed * (time - oldTime) / 1e9;
                break;
            case 'D':
                point.x += speed * (time - oldTime) / 1e9;
                break;
        }
        oldTime = time;
        Rect rect = new Rect(point.x, point.y, 14, 20);
        rect.point.x += (Sprite.SCALED_SIZE - rect.width) / 2;
        rect.point.y += Sprite.SCALED_SIZE - rect.height;
        if (!rect.equals(getRect())){
            if (!checkWallPass(rect)) return;
            if (!checkBombPass(rect)) return;
        }
        position = point;
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
        wallPassTime = BombermanGame.now + time;
        return true;
    }

    @Override
    public boolean checkWallPass(Rect rect) {
        if (BombermanGame.now < wallPassTime) return true;
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
    public boolean checkBombPass(Rect rect) {
        if (BombermanGame.now < bombPassTime) return true;
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

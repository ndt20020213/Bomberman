package uet.oop.bomberman.entities.player;

import javafx.scene.canvas.GraphicsContext;
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
import uet.oop.bomberman.structure.Cell;
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
        long time = world.time;
        Point point = new Point(position.x, position.y);
        switch (status) {
            case 'W':
                position.y -= speed * (time - oldTime) / 1e9;
                break;
            case 'S':
                position.y += speed * (time - oldTime) / 1e9;
                break;
            case 'A':
                position.x -= speed * (time - oldTime) / 1e9;
                break;
            case 'D':
                position.x += speed * (time - oldTime) / 1e9;
                break;
        }
        oldTime = time;
        if (!checkWallPass(getRect())) position = point;
        if (!checkBombPass(getRect())) position = point;
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
        if (key == null) {
            status = ' ';
            return;
        }
        switch (key) {
            case "W":
            case "Up":
                key = "W";
                break;
            case "S":
            case "Down":
                key = "S";
                break;
            case "A":
            case "Left":
                key = "A";
                break;
            case "D":
            case "Right":
                key = "D";
                break;
        }
        if (status == key.charAt(0)) status = ' ';
    }

    public void putBomb() {
        if (this.bomb <= 0) return;
        Bomb bomb = new Bomb(this, flame);
        for (Bomb bomb1 : world.bombs) if (bomb.impact(bomb1)) return;
        world.entities.add(bomb);
    }

    //canDestroy
    private int health = 1;

    @Override
    public void destroy() {
        if (checkFlamePass(health, health - 1)) return;
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
    private int flame = 5;

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
    public boolean checkWallPass(Rect rect) {
        if (world.time < wallPassTime) return true;
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
        bombPassTime = world.time + time;
        return true;
    }

    @Override
    public boolean checkBombPass(Rect rect) {
        if (world.time < bombPassTime) return true;
        for (Bomb bomb : world.bombs)
            if (bomb.getRect().impact(rect))
                if (bomb.getPosition().distance(getPosition()) >= Sprite.SCALED_SIZE * 0.75) return false;
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
    public boolean checkFlamePass(int health, int newHealth) {
        return world.time >= flamePassTime;
    }
}

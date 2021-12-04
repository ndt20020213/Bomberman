package uet.oop.bomberman.entities.player;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.entities.attack.Bomb;
import uet.oop.bomberman.entities.attack.effects.canDestroy;
import uet.oop.bomberman.entities.background.*;
import uet.oop.bomberman.entities.player.effects.*;
import uet.oop.bomberman.entities.player.properties.*;
import uet.oop.bomberman.network.IConnected;
import uet.oop.bomberman.structure.*;

public class Bomber extends Entity implements canDestroy,
        SpeedProperty, BombProperty, FlameProperty, HealthProperty,
        WallPassEffect, BombPassEffect, FlamePassEffect {

    public Bomber(int x, int y) {
        super(x, y, Sprite.player_down.getFxImage());
    }

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    // Di chuyển và trạng thái.
    private long oldTime = 0;

    private String status = "Stand";    // Stand Move Dead
    private char direction = 'D';       // W D S A

    private final int deadCircle = (int) 8e8;   // Đơn vị: ns

    @Override
    public void update() {
        long time = world.time;
        if (status.equals("Move")) {
            Point oldPosition = new Point(position.x, position.y);
            Rect oldRect = getRect();
            switch (direction) {
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
            Rect newRect = getRect();
            if (!newRect.equals(oldRect)) {
                if (!checkWallPass(oldRect, newRect)) position = oldPosition;
                if (!checkBombPass(oldRect, newRect)) position = oldPosition;
            }
        } else if (status.equals("Dead")) {
            if (deadTime > 0 && world.time > deadTime + deadCircle) world.removeEntity(this);
        }
        oldTime = time;
    }

    @Override
    public void render(GraphicsContext gc) {
        int time = (int) (world.time / 1e6);
        if (status.equals("Move")) {
            final int circle = 1500 * Sprite.SCALED_SIZE / speed;     // Đơn vị: ms
            switch (direction) {
                case 'W':
                    img = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, time, circle).getFxImage();
                    break;
                case 'S':
                    img = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, time, circle).getFxImage();
                    break;
                case 'A':
                    img = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, time, circle).getFxImage();
                    break;
                case 'D':
                    img = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, time, circle).getFxImage();
            }
        } else if (status.equals("Stand")) {
            switch (direction) {
                case 'W':
                    img = Sprite.player_up.getFxImage();
                    break;
                case 'S':
                    img = Sprite.player_down.getFxImage();
                    break;
                case 'A':
                    img = Sprite.player_left.getFxImage();
                    break;
                case 'D':
                    img = Sprite.player_right.getFxImage();
                    break;
            }
        } else {
            int dentaTime = (int) (world.time - deadTime);
            if (dentaTime <= deadCircle)
            img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, dentaTime, deadCircle).getFxImage();
        }
        gc.drawImage(img, position.x + 4, position.y);
    }

    // IConnected

    @Override
    public IConnected update(String status) {
        String[] data = status.split(" ");
        position.x = Integer.parseInt(data[0]);
        position.y = Integer.parseInt(data[1]);
        this.status = data[2];
        if (this.status.equals("Dead")) deadTime = world.time;
        direction = data[3].charAt(0);
        return this;
    }

    @Override
    public String toString() {
        return position.getX() + " " + position.getY() + " " + status + " " + direction;
    }

    // Hàm hỗ trợ
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

    // Điều khiển
    public void keyPressed(String key) {
        switch (key) {
            case "W":
            case "Up":
                status = "Move";
                direction = 'W';
                break;
            case "S":
            case "Down":
                status = "Move";
                direction = 'S';
                break;
            case "A":
            case "Left":
                status = "Move";
                direction = 'A';
                break;
            case "D":
            case "Right":
                status = "Move";
                direction = 'D';
                break;
            case "Space":
                putBomb();
                break;
        }
    }

    public void keyReleased(String key) {
        if (key == null) {
            status = "Stand";
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
        if (status.equals("Move") && direction == key.charAt(0)) status = "Stand";
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
    private long deadTime = -1L;

    @Override
    public void destroy() {
        if (checkFlamePass()) health--;
        if (health <= 0) {
            status = "Dead";
            deadTime = world.time;
        }
        System.out.println(health);
    }

    //SpeedProperty
    private int speed = 96;    // Đơn vị pixel/s:  96pixel/s = 3cell/s

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

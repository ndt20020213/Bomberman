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

    public boolean playing = false;

    public Sound sound = new Sound();


    @Override
    public void update() {

        long time = world.time;
        if (status.equals("Move")) {
            Point oldPosition = new Point(position.x, position.y);
            Rect oldRect = getRect();
            switch (direction) {
                case 'W':
                    position.y -= speed * (time - oldTime) / 1e9;
                    if (time > oldTime + 1e8/5) {
                        sound.setFile(0);
                        sound.play();
                    }
                    break;
                case 'S':
                    position.y += speed * (time - oldTime) / 1e9;
                    if (time > oldTime + 1e8/5) {
                        sound.setFile(0);
                        sound.play();
                    }
                    break;
                case 'A':
                    position.x -= speed * (time - oldTime) / 1e9;
                    if (time > oldTime + 1e8/5) {
                        sound.setFile(0);
                        sound.play();
                    }
                    break;
                case 'D':
                    position.x += speed * (time - oldTime) / 1e9;
                    if (time > oldTime + 1e8/5) {
                        sound.setFile(0);
                        sound.play();
                    }
                    break;
            }
            if (oldPosition.x != position.x || oldPosition.y != position.y) {
                if (checkWallPass(oldPosition, oldRect))
                    checkBombPass(oldPosition, oldRect);
            }
        } else if (status.equals("Dead"))
            if (deadTime > 0 && world.time > deadTime + deadCircle) world.removeEntity(this);
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
        if (status.equals("Dead")) return;
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
                sound.setFile(3);
                sound.play();
                break;
        }
    }

    public void keyReleased(String key) {
        if (status.equals("Dead")) return;
        if (key == null) {
            status = "Stand";
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

    @Override
    public boolean kill(int health) {
        this.health -= health;
        if (this.health <= 0) {
            status = "Dead";
            deadTime = world.time;
        }
        return true;
    }

    @Override
    public boolean kill() {
        health = 0;
        status = "Dead";
        deadTime = world.time;
        return true;
    }

    //canDestroy
    private long deadTime = -1L;

    @Override
    public void destroy() {
        if (world.time > flamePassTime) health--;
        if (this.health <= 0) {
            status = "Dead";
            deadTime = world.time;
        }
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
    public boolean addWallPass(int time) {
        wallPassTime = (long) (world.time + time * 1e9);
        return true;
    }

    public boolean checkWallPass(Point oldPosition, Rect oldRect) {
        Point newPosition = position;
        Rect newRect = getRect();
        Entity entity = null;
        for (Wall wall : world.walls)
            if (!wall.getRect().impact(oldRect) && wall.getRect().impact(newRect)) {
                entity = wall;
                break;
            }
        if (entity == null)
            for (Brick brick : world.bricks)
                if (!brick.getRect().impact(oldRect) && brick.getRect().impact(newRect)) {
                    entity = brick;
                    break;
                }
        // Không va chạm
        if (entity == null) return true;
        // Không có Item
        if (world.time > wallPassTime) {
            position = oldPosition;
            return false;
        }
        // Vượt tường
        Point vector = new Point(newPosition.x - oldPosition.x, newPosition.y - oldPosition.y);
        double vectorLength = vector.distance(new Point(0, 0));
        vector.x /= vectorLength;
        vector.y /= vectorLength;
        position.x += (entity.getRect().width + oldRect.width) * vector.x;
        position.y += (entity.getRect().height + oldRect.height) * vector.y;
        for (Wall wall : world.walls)
            if (impact(wall)) {
                position = oldPosition;
                return false;
            }
        for (Brick brick : world.bricks)
            if (impact(brick)) {
                position = oldPosition;
                return false;
            }
        return true;
    }

    //BombPassEffect
    private long bombPassTime = 0;

    @Override
    public boolean addBombPass(int time) {
        bombPassTime = (long) (world.time + time * 1e9);
        return true;
    }

    public boolean checkBombPass(Point oldPosition, Rect oldRect) {
        Rect newRect = getRect();
        if (world.time <= bombPassTime) return true;
        for (Bomb bomb : world.bombs)
            if (!bomb.getRect().impact(oldRect) && bomb.getRect().impact(newRect)) {
                position = oldPosition;
                return false;
            }
        return true;
    }

    //FlamePassEffect
    private long flamePassTime = 0;

    @Override
    public boolean addFlamePass(int time) {
        flamePassTime = (long) (world.time + time * 1e9);
        return true;
    }
}

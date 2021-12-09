package uet.oop.bomberman.entities.enemies;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;
import uet.oop.bomberman.structure.Point;

import java.util.ArrayList;
import java.util.List;

public class Kondoria extends Enemy {
    public static final double NormalSpeed = 1;
    public static final double HighSpeed = 1.5;

    private int status = 0;

    private Cell MoveEnd;

    public Kondoria(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.oneal_right1.getFxImage());
    }

    @Override
    public void update() {

        if (direction.equals("Dead")) {
            if (world.time >= timed + 8e8) {
                world.addEntity(new Oneal(getUnit().x,getUnit().y));
                world.addEntity(new Oneal(getUnit().x,getUnit().y));
                world.removeEntity(this);
            }
            return;
        }

        double speed;

        if (status == 0) {
            speed = NormalSpeed;
        } else {
            speed = HighSpeed;
        }

        if (Move.isEmpty()) {
            getMove();
        }

        if (Move.isEmpty()) return;

        Point move = new Point(Move.peek());

        if (position.x < move.x) {
            direction = "Right";
            if (position.x + speed >= move.x) {
                position.x = move.x;
                Move.poll();
                if (!Move.isEmpty()) {
                    if (entitiesMatrix[Move.peek().x][Move.peek().y] != null) {
                        Move.clear();
                    }
                }
            } else {
                position.x += speed;
            }
        } else if (position.x > move.x) {
            direction = "Left";
            if (position.x - speed <= move.x) {
                position.x = move.x;
                Move.poll();
                if (!Move.isEmpty()) {
                    if (entitiesMatrix[Move.peek().x][Move.peek().y] != null) {
                        Move.clear();
                    }
                }
            } else {
                position.x -= speed;
            }
        } else if (position.y < move.y) {
            if (position.y + speed >= move.y) {
                position.y = move.y;
                Move.poll();
                if (!Move.isEmpty()) {
                    if (entitiesMatrix[Move.peek().x][Move.peek().y] != null) {
                        Move.clear();
                    }
                }
            } else {
                position.y += speed;
            }
        } else if (position.y > move.y) {
            if (position.y - speed <= move.y) {
                position.y = move.y;
                Move.poll();
                if (!Move.isEmpty()) {
                    if (entitiesMatrix[Move.peek().x][Move.peek().y] != null) {
                        Move.clear();
                    }
                }
            } else {
                position.y -= speed;
            }
        } else {
            Move.poll();
            if (!Move.isEmpty()) {
                if (entitiesMatrix[Move.peek().x][Move.peek().y] != null) {
                    Move.clear();
                }
            }
        }

        if (world.time >= timed + 3e9) {
            for (Bomber bomber : world.bombers) {
                if (impact(bomber)) {
                    timed = world.time;
                    bomber.kill(1);
                }
            }
        }
    }

    private void getMove() {
        Cell cell = super.getUnit();

        double distance = 140;
        Cell end = null;
        if (world.time >= timed + 3e9) {
            for (Bomber bomber : world.bombers) {
                double x = position.distance(bomber.getPosition());
                if (x < distance) {
                    distance = x;
                    end = bomber.getUnit();
                }
            }
        }
        if (end != null) {
            if (end.equals(MoveEnd)) return;
            else {
                MoveEnd = end;
                status = 1;
                List<Cell> move = new ArrayList<>();
                move.add(cell);
                if (FindTheWay(cell, end, move)) {
                    Move.clear();
                    Move.addAll(move);
                    Move.poll();
                    return;
                }
            }
        }

        MoveEnd = null;

        status = 0;


        if (Move.isEmpty()) {
            MoveRandom();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        int time = (int) (world.time / 1e6);
        final int circle = 1500 * Sprite.SCALED_SIZE / 96;
        switch (direction) {
            case "Left":
                img = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, time, circle).getFxImage();
                break;
            case "Right":
                img = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, time, circle).getFxImage();
                break;
            case "Dead":
                img = Sprite.kondoria_dead.getFxImage();
                break;
        }
        gc.drawImage(img, position.x, position.y);
    }

}

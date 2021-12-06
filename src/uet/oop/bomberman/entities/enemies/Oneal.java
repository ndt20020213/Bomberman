package uet.oop.bomberman.entities.enemies;

import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;
import uet.oop.bomberman.structure.Point;
import uet.oop.bomberman.structure.Rect;

import java.util.ArrayList;
import java.util.List;

public class Oneal extends Enemy {
    public static final double NormalSpeed = 1;
    public static final double HighSpeed = 1.5;

    private int status = 0;

    private Cell MoveEnd;

    public Oneal(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.oneal_right1.getFxImage());
    }

    @Override
    public void update() {

        double speed;

        if (status == 0) {
            speed = NormalSpeed;
        } else {
            speed = HighSpeed;
        }
        if (Move.isEmpty()) getMove();
        if (Move.isEmpty()) return;

        Point move = new Point(Move.peek());

        if (entitiesMatrix[Move.peek().x][Move.peek().y] != null) {
            Move.clear();
            getMove();
        }

        if (position.x < move.x) {
            if (position.x + speed >= move.x) {
                position.x = move.x;
                Move.poll();

            } else {
                position.x += speed;
            }
        } else if (position.x > move.x) {
            if (position.x - speed <= move.x) {
                position.x = move.x;
                Move.poll();
            } else {
                position.x -= speed;
            }
        } else if (position.y < move.y) {
            if (position.y + speed >= move.y) {
                position.y = move.y;
                Move.poll();
            } else {
                position.y += speed;
            }
        } else if (position.y > move.y) {
            if (position.y - speed <= move.y) {
                position.y = move.y;
                Move.poll();
            } else {
                position.y -= speed;
            }
        } else {
            Move.poll();
        }
    }

    private void getMove() {
        Cell cell = super.getUnit();

        double distance = 140;
        Cell end = null;
        for (Bomber bomber : world.bombers) {
            double x = position.distance(bomber.getPosition());
            if (x < distance) {
                distance = x;
                end = bomber.getUnit();
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
                    System.out.println("Successfully!");
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
    public void destroy() {
        hp--;
    }
}

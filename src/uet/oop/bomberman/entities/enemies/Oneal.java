package uet.oop.bomberman.entities.enemies;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;
import uet.oop.bomberman.structure.Point;

import java.util.ArrayList;
import java.util.Stack;

public class Oneal extends Enemy {
    public static final double NormalSpeed = 1;
    public static final double HighSpeed = 1.5;

    public Oneal(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.oneal_right1.getFxImage());
    }

    @Override
    public void update() {
        Cell cell = super.getUnit();

        double distance = 200;
        Cell end = null;
        for (Bomber bomber : world.bombers) {
            double x = position.distance(bomber.getPosition());
            if (x < distance) {
                distance = x;
                end = bomber.getUnit();
            }
        }
        if (end != null) {
            Stack<Cell> move = new Stack<>();
            if (FindTheWay(cell, end, 0, move)) {
                Move = move;
            }
        }

        if (Move.isEmpty()) {
            MoveRandom();
        }

        Point move = new Point(Move.peek());

        if (position.x < move.x) {
            if (position.x + NormalSpeed >= move.x) {
                position.x = move.x;
                Move.pop();
            } else {
                position.x += NormalSpeed;
            }
        } else if (position.x > move.x) {
            if (position.x - NormalSpeed <= move.x) {
                position.x = move.x;
                Move.pop();
            } else {
                position.x -= NormalSpeed;
            }
        } else if (position.y < move.y) {
            if (position.y + NormalSpeed >= move.y) {
                position.y = move.y;
                Move.pop();
            } else {
                position.y += NormalSpeed;
            }
        } else if (position.y > move.y) {
            if (position.y - NormalSpeed <= move.y) {
                position.y = move.y;
                Move.pop();
            } else {
                position.y -= NormalSpeed;
            }
        }
    }

    @Override
    public void destroy() {
        hp--;
    }
}

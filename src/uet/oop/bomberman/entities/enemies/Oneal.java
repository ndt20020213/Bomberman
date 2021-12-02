package uet.oop.bomberman.entities.enemies;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;
import uet.oop.bomberman.structure.Point;

import java.util.ArrayList;

public class Oneal extends Enemy {
    public static final double NormalSpeed = 1;
    public static final double HighSpeed = 1.5;

    public Oneal(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.oneal_right1.getFxImage());
    }

    @Override
    public void update() {
        Cell cell = super.getUnit();
        /*for (Bomber bomber : world.bombers)
            if (impact(bomber)) {
                return;
            }*/
        Cell end = new Cell(-1,-1);

        double distance = 200;
        for (Bomber bomber : world.bombers) {
            double x = position.distance(bomber.getPosition());
            if (x < distance) {
                distance = x;
                end = bomber.getUnit();
            }
        }
        if (end.x != -1) {
            visited = new int[BombermanGame.WIDTH][BombermanGame.HEIGHT];
            boolean x = FindTheWay(cell, end, 0);
        }

        if (Move.isEmpty()) {
            MoveRandom();
        }
        Cell cmove = Move.get(Move.size() - 1);
        Point pmove = new Point(cmove);

        int x = 0;
        int y = 0;
        if (pmove.x - position.x < 0) x = -1;
        else if (pmove.x - position.x > 0) x = 1;
        if (pmove.y - position.y < 0) y = -1;
        else if (pmove.y - position.y > 0) y = 1;
        position.x += NormalSpeed * x;
        position.y += NormalSpeed * y;
        if (position.distance(pmove) == 0) {
            if (cmove.equals(cell.Above())) {
                MoveHistory.add('U');
            } else if (cmove.equals(cell.Bellow())) {
                MoveHistory.add('D');
            } else if (cmove.equals(cell.Left())) {
                MoveHistory.add('L');
            } else if (cmove.equals(cell.Right())) {
                MoveHistory.add('R');
            }
            Move.remove(Move.size() - 1);
        }
    }

    @Override
    public void destroy() {
        hp--;
    }
}

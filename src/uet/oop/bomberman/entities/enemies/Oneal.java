package uet.oop.bomberman.entities.enemies;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;

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
            if (x < distance) distance = x;
            end = bomber.getUnit();
        }
        if (end.x != -1) {
            visited = new int[BombermanGame.WIDTH][BombermanGame.HEIGHT];
            boolean x = FindTheWay(cell, end, 0);
        }

        if (Move.isEmpty()) {
            MoveRandom();
        }
        Cell move = Move.get(Move.size() - 1);
        position.x += NormalSpeed * (move.x - cell.x);
        position.y += NormalSpeed *(move.y - cell.y);
        Cell newcell = super.getUnit();
        if (newcell.x == move.x && newcell.y == move.y) {
            if (move == cell.Above()) {
                MoveHistory.add('U');
            } else if (move == cell.Bellow()) {
                MoveHistory.add('D');
            } else if (move == cell.Left()) {
                MoveHistory.add('L');
            }else if (move == cell.Right()) {
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

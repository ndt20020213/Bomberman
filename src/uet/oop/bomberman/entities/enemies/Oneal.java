package uet.oop.bomberman.entities.enemies;

import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;

public class Oneal extends Enemy {

    public Oneal(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.oneal_right1.getFxImage());
    }

    @Override
    public void update() {
        Cell cell = super.getUnit();
        if (Move.isEmpty()) {
            MoveRandom();
        }
        Cell move = Move.get(Move.size() - 1);
        int speed = 1;
        position.x += speed * (move.x - cell.x);
        position.y += speed *(move.y - cell.y);
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

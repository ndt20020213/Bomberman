package uet.oop.bomberman.entities.enemies;

import uet.oop.bomberman.entities.background.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;
import uet.oop.bomberman.structure.Point;

public class Balloom extends Enemy {
    public static final double speed = 1;

    public Balloom(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.balloom_right1.getFxImage());
    }

    @Override
    public void update() {
        Cell cell = super.getUnit();
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
        position.x += speed * x;
        position.y += speed * y;
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

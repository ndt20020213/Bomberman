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
        if (Move.empty()) {
            MoveRandom();
        }
        Point move = new Point(Move.peek());
        if (position.x < move.x) {
            if (position.x + speed >= move.x) {
                position.x = move.x;
                Move.pop();
            } else {
                position.x += speed;
            }
        } else if (position.x > move.x) {
            if (position.x - speed <= move.x) {
                position.x = move.x;
                Move.pop();
            } else {
                position.x -= speed;
            }
        } else if (position.y < move.y) {
            if (position.y + speed >= move.y) {
                position.y = move.y;
                Move.pop();
            } else {
                position.y += speed;
            }
        } else if (position.y > move.y) {
            if (position.y - speed <= move.y) {
                position.y = move.y;
                Move.pop();
            } else {
                position.y -= speed;
            }
        }
    }

    @Override
    public void destroy() {
        hp--;
    }
}

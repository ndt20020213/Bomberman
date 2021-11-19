package uet.oop.bomberman.entities.attack;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Point;

/*
Chưa xong theo mẫu.
 */
public class Flame extends Entity {

    private final long startTime;

    private final int length;
    private final int maxLength;

    // Hướng nổ: (-1, 0); (1, 0) hoặc (0, -1); (0, 1).
    private Point vector;

    public Flame(int xUnit, int yUnit, int x,int y, int length, int maxLength) {
        super(xUnit, yUnit, null);
        if (x != 0) {
            img = Sprite.explosion_horizontal.getFxImage();
        } else {
            img = Sprite.explosion_vertical.getFxImage();
        }
        this.length = length;
        this.maxLength = maxLength;
        startTime = world.time;
    }

    @Override
    public void update() {

    }
}

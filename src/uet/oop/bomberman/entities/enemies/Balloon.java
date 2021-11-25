package uet.oop.bomberman.entities.enemies;

import uet.oop.bomberman.graphics.Sprite;

public class Balloon extends Enemy {

    public Balloon(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.balloom_right1.getFxImage());
    }

    @Override
    public void update() {

    }

    @Override
    public void destroy() {
        hp--;
    }
}

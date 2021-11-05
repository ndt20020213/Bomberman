package uet.oop.bomberman.entities.enemies;

import uet.oop.bomberman.graphics.Sprite;

public class Oneal extends Enemy {

    public Oneal(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.oneal_right1.getFxImage());
    }

    @Override
    public void update() {

    }

    @Override
    public void destroy() {
        hp--;
    }
}

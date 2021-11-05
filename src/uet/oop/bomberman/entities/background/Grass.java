package uet.oop.bomberman.entities.background;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class Grass extends Entity {

    public Grass(int x, int y) {
        super(x, y, Sprite.grass.getFxImage());
    }

    @Override
    public final void update() {

    }
}

package uet.oop.bomberman.entities.background;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class Wall extends Entity {

    public Wall(int x, int y) {
        super(x, y, Sprite.wall.getFxImage());
    }

    @Override
    public final void update() {

    }
}

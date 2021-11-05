package uet.oop.bomberman.entities.bricks;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class RedBrick extends Brick {

    private final Entity entity;

    public RedBrick(int x, int y) {
        super(x, y, Sprite.brick_exploded.getFxImage(), 3);
        entity = null;
    }

    public RedBrick(int x, int y, Entity entity) {
        super(x, y, Sprite.brick_exploded.getFxImage(), 3);
        this.entity = entity;
    }

    @Override
    public final void destroy() {
        stiffness--;
        if (stiffness == 2) img = Sprite.brick_exploded1.getFxImage();
        else if (stiffness == 1) img = Sprite.brick_exploded2.getFxImage();
        else {
            //if (entity != null) world.addEntity(entity);
            //world.removeEntity(this);
        }
    }
}

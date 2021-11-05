package uet.oop.bomberman.entities.bricks;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class WhiteBrick extends Brick {

    private final Entity entity;

    public WhiteBrick(int x, int y) {
        super(x, y, Sprite.brick.getFxImage(), 1);
        entity = null;
    }

    public WhiteBrick(int x, int y, Entity entity) {
        super(x, y, Sprite.brick.getFxImage(), 1);
        this.entity = entity;
    }

    @Override
    public final void destroy() {
        if (entity != null) world.addEntity(entity);
        world.removeEntity(this);
    }
}

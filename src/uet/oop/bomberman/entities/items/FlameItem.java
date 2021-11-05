package uet.oop.bomberman.entities.items;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class FlameItem extends Item {

    public static final int flame = 1;

    public FlameItem(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.powerup_flames.getFxImage());
    }

    @Override
    public void use(Entity entity) {

    }
}

package uet.oop.bomberman.entities.items;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class WallPassItem extends Item {

    public WallPassItem(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.powerup_wallpass.getFxImage());
    }

    @Override
    public void use(Entity entity) {

    }
}

package uet.oop.bomberman.entities.items;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.player.effects.WallPassEffect;
import uet.oop.bomberman.graphics.Sprite;

public class WallPassItem extends Item {

    public static final int wallPass = 10;      // Đơn vị s

    public WallPassItem(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.powerup_wallpass.getFxImage());
    }

    @Override
    public void use(Entity entity) {
        if (entity instanceof WallPassEffect) {
            WallPassEffect wallPassEffect = (WallPassEffect) entity;
            if (wallPassEffect.addWallPass(wallPass)) world.removeEntity(this);
        }
    }
}

package uet.oop.bomberman.entities.items;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.player.effects.FlamePassEffect;
import uet.oop.bomberman.graphics.Sprite;

public class FlamePassItem extends Item {

    public static final int flamePass = 10;     // Đơn vị s

    public FlamePassItem(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.powerup_flamepass.getFxImage());
    }

    @Override
    public void use(Entity entity) {
        if (entity instanceof FlamePassEffect) {
            FlamePassEffect flamePassEffect = (FlamePassEffect) entity;
            if (flamePassEffect.addFlamePass(flamePass)) world.removeEntity(this);
        }
    }
}

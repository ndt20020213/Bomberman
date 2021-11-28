package uet.oop.bomberman.entities.items;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.player.properties.BombProperty;
import uet.oop.bomberman.entities.player.properties.HealthProperty;
import uet.oop.bomberman.graphics.Sprite;

public class HealthItem extends Item {

    public static final int bomb = 1;

    public HealthItem(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.powerup_detonator.getFxImage());
    }

    @Override
    public void use(Entity entity) {
        if (entity instanceof HealthProperty) {
            HealthProperty healthProperty = (HealthProperty) entity;
            if (healthProperty.addHealth(bomb)) world.removeEntity(this);
        }
    }
}

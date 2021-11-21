package uet.oop.bomberman.entities.items;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.player.properties.SpeedProperty;
import uet.oop.bomberman.graphics.Sprite;

public class SpeedItem extends Item {

    public static final int speed = 50;

    public SpeedItem(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.powerup_speed.getFxImage());
    }

    @Override
    public void use(Entity entity) {
        if (entity instanceof SpeedProperty) {
            SpeedProperty speedProperty = (SpeedProperty) entity;
            if (speedProperty.addSpeed(speed)) world.removeEntity(this);
        }
    }
}

package uet.oop.bomberman.entities.items;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.player.properties.BombProperty;
import uet.oop.bomberman.graphics.Sprite;

public class BombItem extends Item {

    public static final int bomb = 1;

    public BombItem(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.powerup_bombs.getFxImage());
    }

    @Override
    public void use(Entity entity) {
        if (entity instanceof BombProperty) {
            BombProperty bombProperty = (BombProperty) entity;
            if (bombProperty.addBomb(bomb)) BombermanGame.world.removeEntity(this);
        }

    }
}

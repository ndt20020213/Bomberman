package uet.oop.bomberman.entities.items;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.player.effects.BombPassEffect;
import uet.oop.bomberman.graphics.Sprite;

public class BombPassItem extends Item {

    public static final int bombPass = 2;

    public BombPassItem(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.powerup_bombpass.getFxImage());
    }

    @Override
    public void use(Entity entity) {
        if (entity instanceof BombPassEffect) {
            BombPassEffect bombPassEffect = (BombPassEffect) entity;
            if (bombPassEffect.addBombPass(bombPass)) world.removeEntity(this);
        }
    }
}

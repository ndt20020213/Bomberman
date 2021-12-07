package uet.oop.bomberman.entities;

import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class Portal extends Entity {

    public static Runnable endGame;

    public Portal(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.portal.getFxImage());
    }

    @Override
    public void update() {
        if (world.enemies.size() == 0) {
            for (int i = world.bombers.size() - 1; i >= 0; i--) {
                Bomber bomber = world.bombers.get(i);
                if (impact(bomber)) world.removeEntity(bomber);
            }
            if (world.bombers.size() == 0) endGame.run();
        }
    }
}

package uet.oop.bomberman.entities;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class Portal extends Entity {

    public static int bomberCount = 0;

    public Portal(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.portal.getFxImage());
        bomberCount = 0;
    }

    @Override
    public void update() {
        if (world.enemies.size() == 0) {
            for (int i = world.bombers.size() - 1; i >= 0; i--) {
                Bomber bomber = world.bombers.get(i);
                if (impact(bomber)) {
                    BombermanGame.stopSound(bomber.name, "bomberGo");
                    world.removeEntity(bomber);
                    bomberCount++;
                }
            }
        }
    }
}

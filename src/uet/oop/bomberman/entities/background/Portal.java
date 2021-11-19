package uet.oop.bomberman.entities.background;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

import java.util.function.Consumer;

public class Portal extends Entity {

    public static Consumer<Boolean> endGame;

    public Portal(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.portal.getFxImage());
    }

    @Override
    public void update() {

    }
}

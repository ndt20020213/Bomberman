package uet.oop.bomberman.entities.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.effects.canDestroy;

public abstract class Enemy extends Entity implements canDestroy {
    protected int hp;

    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }
}
package uet.oop.bomberman.entities.bricks;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.effects.canDestroy;

public abstract class Brick extends Entity implements canDestroy {

    protected int stiffness;

    public Brick(int x, int y, Image img, int stiffness) {
        super(x, y, img);
        this.stiffness = stiffness;
    }

    @Override
    public void update() {

    }
}

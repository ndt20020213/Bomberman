package uet.oop.bomberman.entities.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.effects.canDestroy;
import uet.oop.bomberman.structure.Point;

import java.util.Stack;

public abstract class Enemy extends Entity implements canDestroy {
    protected int hp;

    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    /**
     * Di chuyển ngẫu nhiên.
     */
    public Stack<Integer> MoveRandom() {
        return null;
    }
}



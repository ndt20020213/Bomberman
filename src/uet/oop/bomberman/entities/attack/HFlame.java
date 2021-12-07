package uet.oop.bomberman.entities.attack;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.background.Brick;
import uet.oop.bomberman.entities.background.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;

import java.util.ArrayList;
import java.util.HashSet;

public class HFlame extends Flame {

    public HFlame(int xUnit, int yUnit, int length) {
        this(xUnit, yUnit, length, new HashSet<>());
    }

    public HFlame(int xUnit, int yUnit, int length, HashSet<Entity> impactHistory) {
        super(xUnit, yUnit, length, Sprite.explosion_horizontal.getFxImage(), impactHistory);
        if (length == -1) {
            img = Sprite.explosion_horizontal_left_last.getFxImage();
        } else if (length == 1) {
            img = Sprite.explosion_horizontal_right_last.getFxImage();
        }
    }

    @Override
    protected void explosive() {
        exploded = true;
        int length = this.length - this.length / Math.abs(this.length);
        ArrayList<Flame> flames = new ArrayList<>();
        Cell unit = getUnit();

        if (length < 0) flames.add(checkImpact(new HFlame(unit.x - 1, unit.y, length, impactHistory)));
        else if (length > 0) flames.add(checkImpact(new HFlame(unit.x + 1, unit.y, length, impactHistory)));

        for (Flame flame : flames)
            if (flame != null) world.addEntity(flame);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(img, position.x, position.y);
        int dentaTime = (int) (world.time - startTime);
        if (length > 1 || length < -1) {
            img = Sprite.movingSprite(Sprite.explosion_horizontal,
                    Sprite.explosion_horizontal1,
                    Sprite.explosion_horizontal2,
                    dentaTime, circle
            ).getFxImage();
        } else if (length == -1) {
            img = Sprite.movingSprite(Sprite.explosion_horizontal_left_last,
                    Sprite.explosion_horizontal_left_last1,
                    Sprite.explosion_horizontal_left_last2,
                    dentaTime, circle
            ).getFxImage();
        } else if (length == 1) {
            img = Sprite.movingSprite(Sprite.explosion_horizontal_right_last,
                    Sprite.explosion_horizontal_right_last1,
                    Sprite.explosion_horizontal_right_last2,
                    dentaTime, circle
            ).getFxImage();
        }
    }
}

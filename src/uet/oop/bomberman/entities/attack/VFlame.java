package uet.oop.bomberman.entities.attack;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;

import java.util.ArrayList;
import java.util.HashSet;

public class VFlame extends Flame {

    public VFlame(int xUnit, int yUnit, int length) {
        this(xUnit, yUnit, length, new HashSet<>());
    }

    public VFlame(int xUnit, int yUnit, int length, HashSet<Entity> impactHistory) {
        super(xUnit, yUnit, length, Sprite.explosion_vertical.getFxImage(), impactHistory);
        if (length == -1) {
            img = Sprite.explosion_vertical_top_last.getFxImage();
        } else if (length == 1) {
            img = Sprite.explosion_vertical_down_last.getFxImage();
        }
    }

    @Override
    protected void explosive() {
        exploded = true;
        int length = this.length - this.length / Math.abs(this.length);
        ArrayList<Flame> flames = new ArrayList<>();
        Cell unit = getUnit();

        if (length < 0) flames.add(checkImpact(new VFlame(unit.x, unit.y - 1, length, impactHistory)));
        else if (length > 0) flames.add(checkImpact(new VFlame(unit.x, unit.y + 1, length, impactHistory)));

        for (Flame flame : flames)
            if (flame != null) world.addEntity(flame);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(img, position.x, position.y);
        int dentaTime = (int) (world.time - startTime);
        if (length > 1 || length < -1) {
            img = Sprite.movingSprite(Sprite.explosion_vertical,
                    Sprite.explosion_vertical1,
                    Sprite.explosion_vertical2,
                    dentaTime, circle
            ).getFxImage();
        } else if (length == -1) {
            img = Sprite.movingSprite(Sprite.explosion_vertical_top_last,
                    Sprite.explosion_vertical_top_last1,
                    Sprite.explosion_vertical_top_last2,
                    dentaTime, circle
            ).getFxImage();
        } else if (length == 1) {
            img = Sprite.movingSprite(Sprite.explosion_vertical_down_last,
                    Sprite.explosion_vertical_down_last1,
                    Sprite.explosion_vertical_down_last2,
                    dentaTime, circle
            ).getFxImage();
        }
    }
}

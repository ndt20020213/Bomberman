package uet.oop.bomberman.entities.attack;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.background.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;

import java.util.ArrayList;

public class VFlame extends Flame {

    public VFlame(int xUnit, int yUnit, int length) {
        super(xUnit, yUnit, length);
        if (length > 1 || length < -1) {
            img = Sprite.explosion_vertical.getFxImage();
        } else if (length == -1) {
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
        if (length < 0) flames.add(new VFlame(unit.x, unit.y - 1, length));
        else if (length > 0) flames.add(new VFlame(unit.x, unit.y + 1, length));
        for (int i = flames.size() - 1; i >= 0; i--) {
            Flame flame = flames.get(i);
            for (Wall wall : world.walls)
                if (flame.impact(wall)) {
                    flames.remove(flame);
                    break;
                }
        }
        for (Flame flame : flames) world.addEntity(flame);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(img, position.x, position.y);
        int dentaTime = (int) (world.time - startTime);
        if (length > 1 || length < -1) {
            img = Sprite.movingSprite(Sprite.explosion_vertical,
                    Sprite.explosion_vertical1,
                    Sprite.explosion_vertical2,
                    dentaTime, (int) 8e8
            ).getFxImage();
        } else if (length == -1) {
            img = Sprite.movingSprite(Sprite.explosion_vertical_top_last,
                    Sprite.explosion_vertical_top_last1,
                    Sprite.explosion_vertical_top_last2,
                    dentaTime, (int) 8e8
            ).getFxImage();
        } else if (length == 1) {
            img = Sprite.movingSprite(Sprite.explosion_vertical_down_last,
                    Sprite.explosion_vertical_down_last1,
                    Sprite.explosion_vertical_down_last2,
                    dentaTime, (int) 8e8
            ).getFxImage();
        }
    }
}

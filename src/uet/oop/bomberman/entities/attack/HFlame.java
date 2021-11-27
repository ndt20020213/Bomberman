package uet.oop.bomberman.entities.attack;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.background.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;

import java.util.ArrayList;

public class HFlame extends Flame {

    public HFlame(int xUnit, int yUnit, int length) {
        super(xUnit, yUnit, length);
        if (length > 1 || length < -1) {
            img = Sprite.explosion_horizontal.getFxImage();
        } else if (length == -1) {
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
        if (length < 0) flames.add(new HFlame(unit.x - 1, unit.y, length));
        else if (length > 0) flames.add(new HFlame(unit.x + 1, unit.y, length));
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
            img = Sprite.movingSprite(Sprite.explosion_horizontal,
                    Sprite.explosion_horizontal1,
                    Sprite.explosion_horizontal2,
                    dentaTime, (int) 8e8
            ).getFxImage();
        } else if (length == -1) {
            img = Sprite.movingSprite(Sprite.explosion_horizontal_left_last,
                    Sprite.explosion_horizontal_left_last1,
                    Sprite.explosion_horizontal_left_last2,
                    dentaTime, (int) 8e8
            ).getFxImage();
        } else if (length == 1) {
            img = Sprite.movingSprite(Sprite.explosion_horizontal_right_last,
                    Sprite.explosion_horizontal_right_last1,
                    Sprite.explosion_horizontal_right_last2,
                    dentaTime, (int) 8e8
            ).getFxImage();
        }
    }
}

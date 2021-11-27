package uet.oop.bomberman.entities.attack;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.effects.canDestroy;
import uet.oop.bomberman.entities.background.Wall;
import uet.oop.bomberman.entities.bricks.Brick;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;

import java.util.ArrayList;
import java.util.HashSet;

public class Flame extends Entity {

    protected final long startTime;
    protected int length;
    protected boolean exploded = false;
    protected HashSet<Entity> impactHistory = new HashSet<>();

    public Flame(int xUnit, int yUnit, int length) {
        this(xUnit, yUnit, length, Sprite.bomb_exploded.getFxImage());
    }

    public Flame(int xUnit, int yUnit, int length, Image img) {
        super(xUnit, yUnit, img);
        for (Brick brick : world.bricks)
            if (impact(brick)) {
                length /= Math.abs(length);
                brick.destroy();
                break;
            }
        this.length = length;
        startTime = world.time;
    }

    @Override
    public void update() {
        if (world.time > startTime + 8e8) world.removeEntity(this);
        if (world.time > startTime + 1e7 && !exploded) explosive();
        for (int i = world.entities.size() - 1; i >= 0; i--) {
            Entity entity = world.entities.get(i);
            if (entity instanceof canDestroy) {
                if (impact(entity) && !impactHistory.contains(entity)) {
                    ((canDestroy) entity).destroy();
                    impactHistory.add(entity);
                }
            }
        }
    }

    protected void explosive() {
        exploded = true;
        ArrayList<Flame> flames = new ArrayList<>();
        Cell unit = getUnit();
        flames.add(new VFlame(unit.x, unit.y - 1, -length));
        flames.add(new VFlame(unit.x, unit.y + 1, length));
        flames.add(new HFlame(unit.x - 1, unit.y, -length));
        flames.add(new HFlame(unit.x + 1, unit.y, length));
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
        super.render(gc);
        int dentaTime = (int) (world.time - startTime);
        img = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1, Sprite.bomb_exploded2, dentaTime, (int) 8e8).getFxImage();
    }
}

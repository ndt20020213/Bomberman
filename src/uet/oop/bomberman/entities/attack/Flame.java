package uet.oop.bomberman.entities.attack;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.effects.canDestroy;
import uet.oop.bomberman.entities.background.*;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.network.IConnected;
import uet.oop.bomberman.structure.Cell;

import java.util.ArrayList;
import java.util.HashSet;

public class Flame extends Entity {

    protected final int circle = (int) 5e8;

    protected final long startTime;
    protected int length;
    protected boolean exploded = false;
    protected HashSet<Entity> impactHistory;

    public Flame(int xUnit, int yUnit, int length) {
        this(xUnit, yUnit, length, Sprite.bomb_exploded.getFxImage(), new HashSet<>());
    }

    public Flame(int xUnit, int yUnit, int length, Image img, HashSet<Entity> impactHistory) {
        super(xUnit, yUnit, img);
        for (Brick brick : world.bricks)
            if (impact(brick)) {
                length /= Math.abs(length);
                brick.destroy();
                impactHistory.add(brick);
                break;
            }
        this.length = length;
        startTime = world.time;
        this.impactHistory = impactHistory;
    }

    @Override
    public void update() {
        if (world.time > startTime + circle) world.removeEntity(this);
        if (world.time > startTime + 1e7 && !exploded) explosive();
        for (int i = world.entities.size() - 1; i >= 0; i--) {
            Entity entity = world.entities.get(i);
            if (entity instanceof canDestroy && !(entity instanceof Brick)) {
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
        flames.add(new VFlame(unit.x, unit.y - 1, -length, impactHistory));
        flames.add(new VFlame(unit.x, unit.y + 1, length, impactHistory));
        flames.add(new HFlame(unit.x - 1, unit.y, -length, impactHistory));
        flames.add(new HFlame(unit.x + 1, unit.y, length, impactHistory));
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
        img = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1, Sprite.bomb_exploded2, dentaTime, circle).getFxImage();
    }

    @Override
    public IConnected update(String status) {
        String[] data = status.split(" ");
        position.x = Integer.parseInt(data[0]);
        position.y = Integer.parseInt(data[1]);
        length = Integer.parseInt(data[2]);
        return this;
    }

    @Override
    public String toString() {
        return position.getX() + " " + position.getY() + " " + length;
    }
}

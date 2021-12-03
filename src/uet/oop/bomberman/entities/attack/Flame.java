package uet.oop.bomberman.entities.attack;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.effects.canDestroy;
import uet.oop.bomberman.entities.background.Brick;
import uet.oop.bomberman.entities.background.Wall;
import uet.oop.bomberman.entities.enemies.Enemy;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sound;
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

    protected boolean firstImpact = true;
    protected HashSet<Entity> impactHistory;

    public Flame(int xUnit, int yUnit, int length) {
        this(xUnit, yUnit, length, Sprite.bomb_exploded.getFxImage(), new HashSet<>());
    }

    public Flame(int xUnit, int yUnit, int length, Image img, HashSet<Entity> impactHistory) {
        super(xUnit, yUnit, img);
        this.length = length;
        startTime = world.time;
        this.impactHistory = impactHistory;
    }

    @Override
    public void update() {
        if (world.time > startTime + circle) world.removeEntity(this);
        if (world.time > startTime + 1e7 && !exploded) explosive();
        if (firstImpact) {
            for (int i = world.entities.size() - 1; i >= 0; i--) {
                Entity entity = world.entities.get(i);
                if (entity instanceof canDestroy) {
                    if (impact(entity) && !impactHistory.contains(entity)) {
                        ((canDestroy) entity).destroy();
                        impactHistory.add(entity);
                    }
                }
            }
            firstImpact = false;
        } else {
            for (int i = world.bombers.size() - 1; i >= 0; i--) {
                Bomber bomber = world.bombers.get(i);
                if (impact(bomber)) bomber.destroy();
            }
            for (int i = world.enemies.size() - 1; i >= 0; i--) {
                Enemy enemy = world.enemies.get(i);
                if (impact(enemy)) enemy.destroy();
            }
        }
    }

    protected void explosive() {
        exploded = true;
        ArrayList<Flame> flames = new ArrayList<>();
        Cell unit = getUnit();

        flames.add(checkImpact(new VFlame(unit.x, unit.y - 1, -length, impactHistory)));
        flames.add(checkImpact(new VFlame(unit.x, unit.y + 1, length, impactHistory)));
        flames.add(checkImpact(new HFlame(unit.x - 1, unit.y, -length, impactHistory)));
        flames.add(checkImpact(new HFlame(unit.x + 1, unit.y, length, impactHistory)));

        for (Flame flame : flames)
            if (flame != null) world.addEntity(flame);

        Sound sound = Sound.getInstance();
        sound.setFile(4);
        sound.play();
    }

    protected Flame checkImpact(Flame flame) {
        for (Wall wall : world.walls)
            if (flame.impact(wall)) return null;
        for (int j = world.bricks.size() - 1; j >= 0; j--) {
            Brick brick = world.bricks.get(j);
            if (flame.impact(brick)) {
                flame.length /= Math.abs(flame.length);
                return flame;
            }
        }
        return flame;
    }

    @Override
    public void render(GraphicsContext gc) {
        int dentaTime = (int) (world.time - startTime);
        img = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1, Sprite.bomb_exploded2, dentaTime, circle).getFxImage();
        super.render(gc);
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

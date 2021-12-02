package uet.oop.bomberman.container;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.background.*;
import uet.oop.bomberman.entities.attack.Bomb;
import uet.oop.bomberman.entities.attack.Flame;
import uet.oop.bomberman.entities.enemies.Enemy;
import uet.oop.bomberman.entities.items.Item;
import uet.oop.bomberman.entities.player.Bomber;

import java.util.List;
import java.util.function.Function;

public class World {

    public Long time = 0L;

    public final List<Entity> entities;
    public final List<Entity> stillObjects;

    public final List<Wall> walls;
    public final List<Grass> grasses;
    public final List<Brick> bricks;

    public final List<Bomber> bombers;
    public final List<Bomb> bombs;
    public final List<Flame> flames;

    public final List<Item> items;
    public final List<Enemy> enemies;

    public final List<Portal> portals;

    public void update(long time) {
        this.time = time;
        for (int i = entities.size() - 1; i >= 0; i--) entities.get(i).update();
    }

    public void render(GraphicsContext gc) {
        for (Entity stillObject : stillObjects) stillObject.render(gc);
        for (Brick brick : bricks) brick.render(gc);
        for (Entity portal : portals) portal.render(gc);
        for (Entity item : items) item.render(gc);
        for (Entity bomb : bombs) bomb.render(gc);
        for (Entity flame : flames) flame.render(gc);
        for (Entity enemy : enemies) enemy.render(gc);
        for (Entity bomber : bombers) bomber.render(gc);
    }

    public Function<Entity, Boolean> addAction = (Entity t) -> true;
    public Function<Entity, Boolean> removeAction = (Entity t) -> true;

    public boolean addEntity(Entity entity) {
        if (!addAction.apply(entity)) return false;

        if (entity instanceof Wall) {
            ((EntityList<Wall>) walls).data.add((Wall) entity);
            ((EntityList<Entity>) stillObjects).data.add(entity);
            return true;
        }
        if (entity instanceof Grass) {
            ((EntityList<Grass>) grasses).data.add((Grass) entity);
            ((EntityList<Entity>) stillObjects).data.add(entity);
            return true;
        }
        if (entity instanceof Brick) {
            ((EntityList<Brick>) bricks).data.add((Brick) entity);
            ((EntityList<Entity>) entities).data.add(entity);
            return true;
        }
        if (entity instanceof Bomber) {
            ((EntityList<Bomber>) bombers).data.add((Bomber) entity);
            ((EntityList<Entity>) entities).data.add(entity);
            return true;
        }
        if (entity instanceof Bomb) {
            ((EntityList<Bomb>) bombs).data.add((Bomb) entity);
            ((EntityList<Entity>) entities).data.add(entity);
            return true;
        }
        if (entity instanceof Flame) {
            ((EntityList<Flame>) flames).data.add((Flame) entity);
            ((EntityList<Entity>) entities).data.add(entity);
            return true;
        }
        if (entity instanceof Item) {
            ((EntityList<Item>) items).data.add((Item) entity);
            ((EntityList<Entity>) entities).data.add(entity);
            return true;
        }
        if (entity instanceof Enemy) {
            ((EntityList<Enemy>) enemies).data.add((Enemy) entity);
            ((EntityList<Entity>) entities).data.add(entity);
            return true;
        }
        if (entity instanceof Portal) {
            ((EntityList<Portal>) portals).data.add((Portal) entity);
            ((EntityList<Entity>) entities).data.add(entity);
            return true;
        }
        return false;
    }

    public boolean removeEntity(Entity entity) {
        if (!removeAction.apply(entity)) return false;

        if (entity instanceof Wall) {
            ((EntityList<Wall>) walls).data.remove((Wall) entity);
            ((EntityList<Entity>) stillObjects).data.remove(entity);
            return true;
        }
        if (entity instanceof Grass) {
            ((EntityList<Grass>) grasses).data.remove((Grass) entity);
            ((EntityList<Entity>) stillObjects).data.remove(entity);
            return true;
        }
        if (entity instanceof Brick) {
            ((EntityList<Brick>) bricks).data.remove((Brick) entity);
            ((EntityList<Entity>) entities).data.remove(entity);
            return true;
        }
        if (entity instanceof Bomber) {
            ((EntityList<Bomber>) bombers).data.remove((Bomber) entity);
            ((EntityList<Entity>) entities).data.remove(entity);
            return true;
        }
        if (entity instanceof Bomb) {
            ((EntityList<Bomb>) bombs).data.remove((Bomb) entity);
            ((EntityList<Entity>) entities).data.remove(entity);
            return true;
        }
        if (entity instanceof Flame) {
            ((EntityList<Flame>) flames).data.remove((Flame) entity);
            ((EntityList<Entity>) entities).data.remove(entity);
            return true;
        }
        if (entity instanceof Item) {
            ((EntityList<Item>) items).data.remove((Item) entity);
            ((EntityList<Entity>) entities).data.remove(entity);
            return true;
        }
        if (entity instanceof Enemy) {
            ((EntityList<Enemy>) enemies).data.remove((Enemy) entity);
            ((EntityList<Entity>) entities).data.remove(entity);
            return true;
        }
        if (entity instanceof Portal) {
            ((EntityList<Portal>) portals).data.remove((Portal) entity);
            ((EntityList<Entity>) entities).data.remove(entity);
            return true;
        }
        return false;
    }

    public World() {
        entities = new EntityList<>(this);
        stillObjects = new EntityList<>(this);
        walls = new EntityList<>(this);

        grasses = new EntityList<>(this);
        bricks = new EntityList<>(this);
        bombers = new EntityList<>(this);
        bombs = new EntityList<>(this);
        flames = new EntityList<>(this);
        items = new EntityList<>(this);
        enemies = new EntityList<>(this);
        portals = new EntityList<>(this);
    }
}

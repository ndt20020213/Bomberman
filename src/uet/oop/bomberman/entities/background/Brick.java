package uet.oop.bomberman.entities.background;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.effects.canDestroy;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.network.IConnected;

public class Brick extends Entity implements canDestroy {

    private final int deadCircle = (int) 8e8;

    private Long deadTime = -1L;

    private final Entity entity;

    public Brick(int x, int y) {
        super(x, y, Sprite.brick.getFxImage());
        entity = null;
    }

    public Brick(int x, int y, Entity entity) {
        super(x, y, Sprite.brick.getFxImage());
        this.entity = entity;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (deadTime > 0 && world.time < deadTime + deadCircle) {
            int dentaTime = (int) (world.time - deadTime);
            img = Sprite.movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1, Sprite.brick_exploded2, dentaTime, deadCircle).getFxImage();
        }
        super.render(gc);
    }

    @Override
    public void update() {
        if (deadTime > 0 && world.time > deadTime + deadCircle) {
            if (entity != null) world.addEntity(entity);
            world.removeEntity(this);
        }
    }

    @Override
    public IConnected update(String status) {
        String[] data = status.split(" ");
        position.x = Integer.parseInt(data[0]);
        position.y = Integer.parseInt(data[1]);
        if (data.length >= 3 && deadTime < 0) deadTime = world.time;
        return this;
    }

    @Override
    public String toString() {
        if (deadTime < 0) return super.toString();
        else return position.getX() + " " + position.getY() + " dead";
    }

    @Override
    public final void destroy() {
        if (deadTime < 0) deadTime = world.time;
    }
}

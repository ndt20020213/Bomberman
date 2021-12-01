package uet.oop.bomberman.entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.network.IConnected;

public class RedBrick extends Brick {

    private final Entity entity;

    public RedBrick(int x, int y) {
        super(x, y, Sprite.brick_exploded.getFxImage(), 3);
        entity = null;
    }

    public RedBrick(int x, int y, Entity entity) {
        super(x, y, Sprite.brick_exploded.getFxImage(), 3);
        this.entity = entity;
    }

    @Override
    public void render(GraphicsContext gc) {
        switch (stiffness) {
            case 3:
                gc.drawImage(Sprite.brick_exploded.getFxImage(), position.x, position.y);
                break;
            case 2:
                gc.drawImage(Sprite.brick_exploded1.getFxImage(), position.x, position.y);
                break;
            case 1:
                gc.drawImage(Sprite.brick_exploded2.getFxImage(), position.x, position.y);
                break;
            default:
                gc.drawImage(img, position.x, position.y);
                break;
        }
    }

    @Override
    public IConnected update(String status) {
        String[] data = status.split(" ");
        position.x = Integer.parseInt(data[0]);
        position.y = Integer.parseInt(data[1]);
        stiffness = Integer.parseInt(data[2]);
        return this;
    }

    @Override
    public String toString() {
        return position.getX() + " " + position.getY() + " " + stiffness;
    }

    @Override
    public final void destroy() {
        stiffness--;
        if (stiffness <= 0) {
            if (entity != null) world.addEntity(entity);
            world.removeEntity(this);
        }
    }
}

package uet.oop.bomberman.entities.attack;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.effects.canDestroy;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class Bomb extends Entity implements canDestroy {

    private final Bomber bomber;
    private final int length;
    private final long startTime;
    protected boolean exploded = false;

    public Bomb(Bomber bomber, int length) {
        super(bomber.getUnit().x, bomber.getUnit().y, Sprite.bomb.getFxImage());
        this.bomber = bomber;
        this.length = length;
        startTime = world.time;
        bomber.addBomb(-1);
    }

    /**
     * Cập nhật thời gian để nổ.
     */
    @Override
    public void update() {
        if (world.time > startTime + 2e9) explosive();
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        img = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, world.time.intValue(), (int) 1e9).getFxImage();
    }

    /**
     * Xử lý khi bomb nổ.
     */
    private void explosive() {
        if (!exploded) {
            bomber.addBomb(1);
            world.removeEntity(this);
            Flame flame = new Flame(position.getX() / Sprite.SCALED_SIZE, position.getY() / Sprite.SCALED_SIZE, length);
            world.addEntity(flame);
            BombermanGame.playSound(bomber.name, "BombExplode");
            exploded = true;
        }
    }

    /**
     * Bị flame phá hủy => Gọi nổ ngay lập tức.
     */
    @Override
    public void destroy() {
        explosive();
    }
}

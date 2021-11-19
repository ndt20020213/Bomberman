package uet.oop.bomberman.entities.attack;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.effects.canDestroy;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class Bomb extends Entity implements canDestroy {

    private final Bomber bomber;
    private final int length;
    private final long startTime;

    public Bomb(int xUnit, int yUnit, Bomber bomber, int length) {
        super(xUnit, yUnit, Sprite.bomb.getFxImage());
        this.bomber = bomber;
        this.length = length;
        startTime = world.time;
        //bomber.addBomb(-1);
    }

    /**
     * Cập nhật thời gian để nổ.
     */
    @Override
    public void update() {
        if (world.time > startTime + 2e9) explosive();
    }

    /**
     * Xử lý khi bomb nổ.
     */
    private void explosive() {
        //bomber.addBomb(1);
    }

    /**
     * Bị flame phá hủy => Gọi nổ ngay lập tức.
     */
    @Override
    public void destroy() {
        explosive();
    }
}

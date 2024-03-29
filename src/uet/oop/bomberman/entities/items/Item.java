package uet.oop.bomberman.entities.items;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.player.Bomber;

public abstract class Item extends Entity {

    public Item(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    /**
     * Kiểm tra va chạm để sử dụng item.
     */
    @Override
    public void update() {
        for (Bomber bomber : world.bombers)
            if (impact(bomber)) {
                use(bomber);
                BombermanGame.playSound(bomber.name, "getExtraItems");
                return;
            }
    }

    /**
     * Sử dụng item cho entity.
     */
    public abstract void use(Entity entity);
}

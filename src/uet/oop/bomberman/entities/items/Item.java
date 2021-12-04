package uet.oop.bomberman.entities.items;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sound;

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
                Sound sound = Sound.getInstance();
                sound.setFile(1);
                sound.play();
                return;
            }
    }

    /**
     * Sử dụng item cho entity.
     */
    public abstract void use(Entity entity);
}

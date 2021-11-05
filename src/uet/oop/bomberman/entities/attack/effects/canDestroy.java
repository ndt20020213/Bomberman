package uet.oop.bomberman.entities.attack.effects;

/**
 * Vật thể có thể bị phá hủy bởi flame.
 */
public interface canDestroy {

    /**
     * Được Flame gọi khi phá hủy.
     */
    void destroy();
}

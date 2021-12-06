package uet.oop.bomberman.entities.player.properties;

public interface HealthProperty {

    /**
     * Thêm máu.
     * @param health : Số health tối đa được cộng.
     * @return true : Nếu ăn item.
     */
    boolean addHealth(int health);

    /**
     * Giết.
     * @param health số máu mất.
     * @return true : Nếu được phép.
     */
    boolean kill(int health);

    /**
     * Giết ngay lập tức = kill(9999).
     * @return true : nếu được phép.
     */
    boolean kill();
}

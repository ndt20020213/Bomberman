package uet.oop.bomberman.entities.player.properties;

public interface HealthProperty {

    /**
     * @param health : Số health tối đa được cộng.
     * @return true : Nếu ăn item.
     */
    boolean addHealth(int health);

}

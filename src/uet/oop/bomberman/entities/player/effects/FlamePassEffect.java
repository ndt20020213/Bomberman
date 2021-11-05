package uet.oop.bomberman.entities.player.effects;

public interface FlamePassEffect {

    /**
     * Thêm thời gian bất tử flame.
     * Được item gọi để sử dụng item.
     * Return true nếu Entity sử dụng được item, ngược lại return false.
     */
    boolean addFlamePass(long time);

    boolean checkFlamePass(int health, int newHealth);

}

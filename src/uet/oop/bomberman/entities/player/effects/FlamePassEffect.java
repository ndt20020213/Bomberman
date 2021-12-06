package uet.oop.bomberman.entities.player.effects;

public interface FlamePassEffect {

    /**
     * Thêm thời gian bất tử flame.
     * Được item gọi để sử dụng item.
     * @param time : Thời gian được cộng, đơn vị s.
     * @return true : Nếu Entity sử dụng được item, ngược lại return false.
     */
    boolean addFlamePass(int time);
}

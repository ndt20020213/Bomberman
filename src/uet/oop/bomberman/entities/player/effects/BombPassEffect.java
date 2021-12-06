package uet.oop.bomberman.entities.player.effects;

public interface BombPassEffect {

    /**
     * Thêm thời gian vượt bomb.
     * Được item gọi để sử dụng item.
     * @param time : Thời gian được cộng, đơn vị s.
     * Return true : Nếu Entity sử dụng được item, ngược lại return false.
     */
    boolean addBombPass(int time);
}

package uet.oop.bomberman.entities.player.effects;

public interface WallPassEffect {

    /**
     * Thêm thời gian vượt tường.
     * Được item gọi để sử dụng item.
     * @param time : Thời gian được cộng, đơn vị s.
     * @return true : Nếu Entity sử dụng được item, ngược lại return false.
     */
    boolean addWallPass(int time);
}

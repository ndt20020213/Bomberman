package uet.oop.bomberman.entities.player.effects;

import uet.oop.bomberman.structure.Rect;

public interface WallPassEffect {

    /**
     * Thêm thời gian vượt tường.
     * Được item gọi để sử dụng item.
     * Return true nếu Entity sử dụng được item, ngược lại return false.
     */
    boolean addWallPass(long time);

    /**
     * Kiểm tra va chạm với vật thể cố định.
     */
    boolean checkWallPass(Rect newPosition);

}

package uet.oop.bomberman.entities.player.effects;

import uet.oop.bomberman.structure.Rect;

public interface BombPassEffect {

    /**
     * Thêm thời gian vượt bomb.
     * Được item gọi để sử dụng item.
     * Return true nếu Entity sử dụng được item, ngược lại return false.
     */
    boolean addBombPass(long time);

    boolean checkBombPass(Rect newPosition);

}

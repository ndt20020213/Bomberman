package uet.oop.bomberman.structure;

import java.util.List;
import java.util.Random;

public class Point {

    // Tọa độ theo phương x.
    public double x;

    // Tọa độ theo phương y.
    public double y;

    public Point() {

    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Chuyển Cell sang Point.
     */
    public Point(Cell cell) {
        this.x = cell.x * Cell.SIZE;
        this.y = cell.y * Cell.SIZE;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    /**
     * Lấy khoảng cách 2 điểm.
     */
    public double distance(Point other) {
        double diff_x = x - other.x;
        double diff_y = y - other.y;
        return Math.sqrt(Math.pow(diff_x, 2) + Math.pow(diff_y, 2));
    }
}

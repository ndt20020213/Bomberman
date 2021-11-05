package uet.oop.bomberman.structure;

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

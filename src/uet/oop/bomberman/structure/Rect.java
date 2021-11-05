package uet.oop.bomberman.structure;

import java.util.Objects;

public class Rect {

    // Tọa độ điểm đầu.
    public Point point;

    public double width;

    public double height;

    public Rect() {
        point = new Point(0, 0);
    }

    public Rect(double x, double y, double w, double h) {
        point = new Point(x, y);
        width = w;
        height = h;
    }

    /**
     * Kiểm tra va chạm 2 hình chữ nhật.
     */
    public boolean impact(Rect other) {
        if (other.point.x >= point.x + width) return false;
        if (other.point.x + other.width <= point.x) return false;
        if (other.point.y >= point.y + height) return false;
        if (other.point.y + other.height <= point.y) return false;
        return true;
    }

    public boolean equals(Rect other) {
        if (point.x != other.point.x) return false;
        if (point.y != other.point.y) return false;
        if (width != other.width) return false;
        if (height != other.height) return false;
        return true;
    }
}

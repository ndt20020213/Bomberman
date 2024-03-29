package uet.oop.bomberman.structure;

import uet.oop.bomberman.graphics.Sprite;

import java.util.concurrent.Callable;

public class Cell {
    public static final int SIZE = Sprite.SCALED_SIZE;
    public int x;
    public int y;

    public Cell() {

    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Chuyển từ Point sang Cell
     */
    public Cell(Point point) {
        this.x = point.getX() / SIZE;
        this.y = point.getY() / SIZE;
    }

    /**
     * Ô phía trên.
     */
    public Cell Above() {
        return new Cell(this.x, this.y - 1);
    }

    /**
     * Ô phía dưới
     */
    public Cell Bellow() {
        return new Cell(this.x, this.y + 1);
    }

    /**
     * Ô bên trái.
     */
    public Cell Left() {
        return new Cell(this.x - 1, this.y);
    }

    /**
     * Ô bên phải.
     */
    public Cell Right() {
        return new Cell(this.x + 1, this.y);
    }

    public boolean equals(Object o) {
        if (o instanceof Cell) {
            Cell cell = (Cell) o;
            if (this.x == cell.x && this.y == cell.y) {
                return true;
            }
        }
        return false;
    }

}

package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.container.World;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.network.IConnected;
import uet.oop.bomberman.structure.Cell;
import uet.oop.bomberman.structure.Point;
import uet.oop.bomberman.structure.Rect;

public abstract class Entity implements IConnected {

    //Tọa độ tính từ góc trái trên trong Canvas
    protected Point position;

    protected Image img;

    // Tham chiếu tới World.
    protected final World world;

    //Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    public Entity(int xUnit, int yUnit, Image img) {
        position = new Point(xUnit * Sprite.SCALED_SIZE, yUnit * Sprite.SCALED_SIZE);
        this.img = img;
        world = BombermanGame.world;
    }

    // Lấy ô tương ứng
    public Cell getUnit() {
        return new Cell(position);
    }

    // Lấy vị trí trên world
    public Point getPosition() {
        return new Point(position.x, position.y);
    }

    // Lấy khung hình ảnh
    public Rect getRect() {
        return new Rect(position.x, position.y, img.getWidth(), img.getHeight());
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(img, position.x, position.y);
    }

    public abstract void update();

    // IConnected

    private int key = -1;

    @Override
    public final boolean setKey(int key) {
        if (this.key < 0) {
            this.key = key;
            return true;
        } return false;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public IConnected update(String status) {
        String[] data = status.split(" ");
        position.x = Integer.parseInt(data[0]);
        position.y = Integer.parseInt(data[1]);
        return this;
    }

    @Override
    public String toString() {
        return position.getX() + " " + position.getY();
    }

    // Impact
    public boolean impact(Entity other) {
        return getRect().impact(other.getRect());
    }
}

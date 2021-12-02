package uet.oop.bomberman.entities.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.container.MatrixWorld;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.effects.canDestroy;
import uet.oop.bomberman.structure.Cell;
import uet.oop.bomberman.structure.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public abstract class Enemy extends Entity implements canDestroy {
    protected int hp;

    /* MoveHistory sử dụng kí hiệu 'U' là di chuyển lên,
     'D' là xuống, 'R' là sang phải, 'L' là sang trái. */
    protected Stack<Cell> MoveHistory = new Stack<>();

    /* Lưu các bước sẽ đi */
    protected Stack<Cell> Move = new Stack<>();

    protected Entity[][] entitiesMatrix = null;

    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        if (world instanceof MatrixWorld) entitiesMatrix = ((MatrixWorld) world).entitiesMatrix;
    }

    /**
     * Tạo bước di chuyển ngẫu nhiên.
     */
    public void MoveRandom() {
        Cell cell = super.getUnit();
        List<Cell> random = new ArrayList<>();
        if (MoveHistory.empty()) {
            MoveHistory.add(cell.Above());
        }
        if (entitiesMatrix[cell.Above().x][cell.Above().y] == null && !MoveHistory.peek().equals(cell.Above())) {
            random.add(cell.Above());
        }
        if (entitiesMatrix[cell.Bellow().x][cell.Bellow().y] == null && !MoveHistory.peek().equals(cell.Bellow())) {
            random.add(cell.Bellow());
        }
        if (entitiesMatrix[cell.Left().x][cell.Left().y] == null && !MoveHistory.peek().equals(cell.Left())) {
            random.add(cell.Left());
        }
        if (entitiesMatrix[cell.Right().x][cell.Right().y] == null && !MoveHistory.peek().equals(cell.Right())) {
            random.add(cell.Right());
        }
        if (random.size() == 0) {
            Move.add(MoveHistory.peek());
        } else if (random.size() == 1) {
            Move.add(random.get(0));
        } else {
            Random rand = new Random();
            Move.add(random.get(rand.nextInt(random.size())));
        }
        MoveHistory.push(cell);
    }

    public boolean FindTheWay(Cell start,Cell end, int x, Stack<Cell> move) {
        if (start.x == end.x && start.y == end.y) {
            return true;
        }
        return false;
    }
}



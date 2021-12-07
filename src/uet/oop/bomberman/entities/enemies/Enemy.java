package uet.oop.bomberman.entities.enemies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.container.MatrixWorld;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.effects.canDestroy;
import uet.oop.bomberman.structure.Cell;
import uet.oop.bomberman.structure.Point;

import java.sql.Time;
import java.util.*;

public abstract class Enemy extends Entity implements canDestroy {
    protected int hp = 1;

    /* Đánh dấu các mốc thời gian,*/
    protected long timed;

    protected String direction = "Left";

    protected Cell MoveHistory = new Cell(0,0);

    /* Lưu các bước sẽ đi */
    protected Queue<Cell> Move = new LinkedList<>();

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
        random.add(cell.Above());
        random.add(cell.Bellow());
        random.add(cell.Left());
        random.add(cell.Right());
        random.removeIf(element -> entitiesMatrix[element.x][element.y] != null);

        if (random.size() == 0) {
            return;
        } else if (random.size() == 1) {
            Move.add(MoveHistory);
        } else {
            random.removeIf(element -> element.equals(MoveHistory));
            Random rand = new Random();
            Move.add(random.get(rand.nextInt(random.size())));
        }
        MoveHistory = cell;
    }

    public boolean FindTheWay(Cell start,Cell end, List<Cell> move) {

        if (start.equals(end)) {
            return true;
        }

        if (move.size() == 5) {
            return false;
        }

        if (entitiesMatrix[start.Above().x][start.Above().y] == null && !move.contains(start.Above())) {
            move.add(start.Above());
            if (FindTheWay(start.Above(), end, move)) {
                return true;
            } else move.remove(move.size() - 1);
        }

        if (entitiesMatrix[start.Bellow().x][start.Bellow().y] == null && !move.contains(start.Bellow())) {
            move.add(start.Bellow());
            if (FindTheWay(start.Bellow(), end, move)) {
                return true;
            } else move.remove(move.size() - 1);
        }

        if (entitiesMatrix[start.Left().x][start.Left().y] == null && !move.contains(start.Left())) {
            move.add(start.Left());
            if (FindTheWay(start.Left(), end, move)) {
                return true;
            } else move.remove(move.size() - 1);
        }

        if (entitiesMatrix[start.Right().x][start.Right().y] == null && !move.contains(start.Right())) {
            move.add(start.Right());
            if (FindTheWay(start.Right(), end, move)) {
                return true;
            } else move.remove(move.size() - 1);
        }

        return false;
    }
}



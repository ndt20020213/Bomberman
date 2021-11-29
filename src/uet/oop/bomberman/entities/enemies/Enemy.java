package uet.oop.bomberman.entities.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.container.MatrixWorld;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.effects.canDestroy;
import uet.oop.bomberman.structure.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public abstract class Enemy extends Entity implements canDestroy {
    protected int hp;

    /* MoveHistory sử dụng kí hiệu 'U' là di chuyển lên,
     'D' là xuống, 'R' là sang phải, 'L' là sang trái. */
    protected Stack<Character> MoveHistory = new Stack<>();

    /* Lưu các bước sẽ đi */
    protected List<Cell> Move = new ArrayList<>();

    /* Mảng check các ô đã đi.*/
    protected int[][] visited;

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
        List<Character> random = new ArrayList<>();
        if (entitiesMatrix[cell.Above().x][cell.Above().y] == null) {
            random.add('U');
        }
        if (entitiesMatrix[cell.Bellow().x][cell.Bellow().y] == null) {
            random.add('D');
        }
        if (entitiesMatrix[cell.Left().x][cell.Left().y] == null) {
            random.add('L');
        }
        if (entitiesMatrix[cell.Right().x][cell.Right().y] == null) {
            random.add('R');
        }
        if (!MoveHistory.empty()) {
            if (MoveHistory.peek().equals('U')) {
                for (int i = 0; i < random.size(); i++)
                    if (random.get(i) == 'D')
                        random.remove(i);
            } else if (MoveHistory.peek().equals('D')) {
                for (int i = 0; i < random.size(); i++)
                    if (random.get(i) == 'U')
                        random.remove(i);
            } else if (MoveHistory.peek().equals('L')) {
                for (int i = 0; i < random.size(); i++)
                    if (random.get(i) == 'R')
                        random.remove(i);
            } else if (MoveHistory.peek().equals('R')) {
                for (int i = 0; i < random.size(); i++)
                    if (random.get(i) == 'L')
                        random.remove(i);
            }
        }
        if (random.size() == 0) {
            if (MoveHistory.peek().equals('U')) {
                Move.add(cell.Bellow());
            } else if (MoveHistory.peek().equals('D')) {
                Move.add(cell.Above());
            } else if (MoveHistory.peek().equals('L')) {
                Move.add(cell.Right());
            } else if (MoveHistory.peek().equals('R')) {
                Move.add(cell.Left());
            }

        } else if (random.size() == 1) {
            if (random.get(0) == 'U') {
                Move.add(cell.Above());
            } else if (random.get(0) == 'D') {
                Move.add(cell.Bellow());
            } else if (random.get(0) == 'L') {
                Move.add(cell.Left());
            } else if (random.get(0) == 'R') {
                Move.add(cell.Right());
            }
        } else {
            Random rand = new Random();
            char x = random.get(rand.nextInt(random.size()));
            if (x == 'U') {
                Move.add(cell.Above());
            } else if (x == 'D') {
                Move.add(cell.Bellow());
            } else if (x == 'L') {
                Move.add(cell.Left());
            } else if (x == 'R') {
                Move.add(cell.Right());
            }
        }
        if (entitiesMatrix[Move.get(0).x][Move.get(0).y] != null ) System.out.println("ERROR");
    }
}



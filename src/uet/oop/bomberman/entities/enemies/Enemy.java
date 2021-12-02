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
        System.out.println(cell.x + " " + cell.y);
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
                    if (random.get(i).equals('D'))
                        random.remove(i);
            } else if (MoveHistory.peek().equals('D')) {
                for (int i = 0; i < random.size(); i++)
                    if (random.get(i).equals('U'))
                        random.remove(i);
            } else if (MoveHistory.peek().equals('L')) {
                for (int i = 0; i < random.size(); i++)
                    if (random.get(i).equals('R'))
                        random.remove(i);
            } else if (MoveHistory.peek().equals('R')) {
                for (int i = 0; i < random.size(); i++)
                    if (random.get(i).equals('L'))
                        random.remove(i);
            }
        }
        System.out.println(random.size());
        for (int i = 0; i < random.size(); i++) {
            System.out.print(random.get(i) + " ");
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
            if (random.get(0).equals('U')) {
                Move.add(cell.Above());
            } else if (random.get(0).equals('D')) {
                Move.add(cell.Bellow());
            } else if (random.get(0).equals('L')) {
                Move.add(cell.Left());
            } else if (random.get(0).equals('R')) {
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
    }

    public boolean FindTheWay(Cell start,Cell end, int x) {
        if (start.x == end.x && start.y == end.y) {
            return true;
        }
        if (x == 5) return false;
        visited[start.x][start.y] = 1;
        if (entitiesMatrix[start.Above().x][start.Above().y] == null && visited[start.x][start.y] != 1) {
            if (FindTheWay(start.Above(),end,x + 1)) {
                Move.add(start.Above());
                return true;
            }
        }
        if (entitiesMatrix[start.Bellow().x][start.Bellow().y] == null && visited[start.x][start.y] != 1) {
            if (FindTheWay(start.Bellow(),end,x + 1)) {
                Move.add(start.Bellow());
                return true;
            }
        }
        if (entitiesMatrix[start.Left().x][start.Left().y] == null && visited[start.x][start.y] != 1) {
            if (FindTheWay(start.Left(),end,x + 1)) {
                Move.add(start.Left());
                return true;
            }
        }
        if (entitiesMatrix[start.Right().x][start.Right().y] == null && visited[start.x][start.y] != 1) {
            if (FindTheWay(start.Right(),end,x + 1)) {
                Move.add(start.Right());
                return true;
            }
        }
        return false;
    }
}



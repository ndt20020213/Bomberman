package uet.oop.bomberman.entities.enemies;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;
import uet.oop.bomberman.structure.Point;

public class Doll extends Enemy {
    public static final double speed = 1;

    public Doll(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.balloom_right1.getFxImage());
    }

    @Override
    public void update() {
        Cell cell = super.getUnit();
        if (direction.equals("Dead")) {
            if (world.time >= timed + 8e8) {
                world.removeEntity(this);
            }
            return;
        }
        if (world.time >= timed + 30e9) {
            timed = world.time;
            if ((cell.x + cell.y) % 2 == 0 ) {
                world.addEntity(new Balloom(cell.x,cell.y));
            } else {
                world.addEntity(new Minvo(cell.x,cell.y));
            }
        }
        if (Move.isEmpty()) {
            MoveRandom();
        }
        if (Move.peek() == null) {
            return;
        }
        Point move = new Point(Move.peek());
        if (position.x < move.x) {
            direction = "Right";
            if (position.x + speed >= move.x) {
                position.x = move.x;
                Move.poll();
            } else {
                position.x += speed;
            }
        } else if (position.x > move.x) {
            direction = "Left";
            if (position.x - speed <= move.x) {
                position.x = move.x;
                Move.poll();
            } else {
                position.x -= speed;
            }
        } else if (position.y < move.y) {
            if (position.y + speed >= move.y) {
                position.y = move.y;
                Move.poll();
            } else {
                position.y += speed;
            }
        } else if (position.y > move.y) {
            if (position.y - speed <= move.y) {
                position.y = move.y;
                Move.poll();
            } else {
                position.y -= speed;
            }
        }

        if (world.time >= timed + 3e9) {
            for (Bomber bomber : world.bombers) {
                if (impact(bomber)) {
                    timed = world.time;
                    bomber.kill(1);
                }
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        int time = (int) (world.time / 1e6);
        final int circle = 1500 * Sprite.SCALED_SIZE / 96;
        switch (direction) {
            case "Left":
                img = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, time, circle).getFxImage();
                break;
            case "Right":
                img = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, time, circle).getFxImage();
                break;
            case "Dead":
                img = Sprite.doll_dead.getFxImage();
                break;
        }
        gc.drawImage(img, position.x, position.y);
    }

    @Override
    public void destroy() {
        hp--;
        if (hp <= 0) {
            direction = "Dead";
            timed = world.time;
        }
    }
}

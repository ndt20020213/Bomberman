package uet.oop.bomberman.entities.enemies;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Cell;
import uet.oop.bomberman.structure.Point;

public class Balloom extends Enemy {
    public static final double speed = 1;

    public Balloom(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.balloom_right1.getFxImage());
    }

    @Override
    public void update() {

        if (direction.equals("Dead")) {
            if (world.time >= timed + (long) 8e8) {
                world.removeEntity(this);
            }
            return;
        }

        Cell cell = super.getUnit();

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
                img = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, time, circle).getFxImage();
                break;
            case "Right":
                img = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, time, circle).getFxImage();
                break;
            case "Dead":
                img = Sprite.balloom_dead.getFxImage();
                break;
        }
        gc.drawImage(img, position.x, position.y);
    }
}

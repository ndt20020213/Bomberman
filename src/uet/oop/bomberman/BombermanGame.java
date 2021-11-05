package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import uet.oop.bomberman.container.World;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.background.Grass;
import uet.oop.bomberman.entities.background.Portal;
import uet.oop.bomberman.entities.background.Wall;
import uet.oop.bomberman.entities.bricks.RedBrick;
import uet.oop.bomberman.entities.bricks.WhiteBrick;
import uet.oop.bomberman.entities.enemies.Balloom;
import uet.oop.bomberman.entities.enemies.Oneal;
import uet.oop.bomberman.entities.items.BombItem;
import uet.oop.bomberman.entities.items.FlameItem;
import uet.oop.bomberman.entities.items.SpeedItem;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.graphics.Sprite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BombermanGame extends Application {

    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;

    public static final World world = new World();
    public static long now = 0;

    private GraphicsContext gc;
    private Canvas canvas;

    private int level = 1;


    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        Scene scene = new Scene(root);

        // Them scene vao stage
        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                BombermanGame.now = l;
                render();
                update();
            }
        };
        timer.start();

        createMap();

        Entity bomberman = new Bomber(1, 1);
        world.entities.add(bomberman);
    }

    public void createMap() {
        File file = new File("res/levels/Level" + level + ".txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Load map error!");
            System.exit(-1);
            return;
        }
        String info = scanner.nextLine();
        int L = Integer.getInteger(info.split(" ")[0], 1);
        int R = Integer.getInteger(info.split(" ")[1], 13);
        int C = Integer.getInteger(info.split(" ")[2], 31);
        for (int i = 0; i < R; i++) {
            String temp = scanner.nextLine();
            for (int j = 0; j < C; j++) {
                char c = temp.charAt(j);
                if (c == '#') world.addEntity(new Wall(j, i));
                else world.addEntity(new Grass(j, i));
                switch (c) {
                    case '*':
                        world.addEntity(new WhiteBrick(j, i));
                        break;
                    case 'x':
                        world.addEntity(new RedBrick(j, i, new Portal(j, i)));
                        break;
                    //Enemies
                    case '1':
                        world.addEntity(new Balloom(j, i));
                        break;
                    case '2':
                        world.addEntity(new Oneal(j, i));
                        break;
                    //Items.
                    case 'b':
                        world.addEntity(new RedBrick(j, i, new BombItem(j, i)));
                        break;
                    case 'f':
                        world.addEntity(new RedBrick(j, i, new FlameItem(j, i)));
                        break;
                    case 's':
                        world.addEntity(new RedBrick(j, i, new SpeedItem(j, i)));
                        break;
                }
            }
        }
    }

    public void update() {
        world.entities.forEach(Entity::update);
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        world.stillObjects.forEach(g -> g.render(gc));
        world.entities.forEach(g -> g.render(gc));
    }
}

package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import uet.oop.bomberman.container.MatrixWorld;
import uet.oop.bomberman.container.World;
import uet.oop.bomberman.display.MenuController;
import uet.oop.bomberman.entities.background.*;
import uet.oop.bomberman.entities.enemies.*;
import uet.oop.bomberman.entities.items.*;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.network.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class BombermanGame extends Application {

    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;

    public static final World world = new MatrixWorld(WIDTH, HEIGHT);

    private MenuController menuController;

    private GraphicsContext gc;
    private Canvas canvas;

    private Scene scene;

    private Connection connection;

    private int level = 1;

    public static boolean isMove = true;

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Tao Menu
        FXMLLoader menuView = new FXMLLoader(getClass().getResource("/fxml/Menu-view.fxml"));
        GridPane menu = menuView.load();

        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);

        // Tao root container
        HBox root = new HBox();
        root.getChildren().add(menu);
        root.getChildren().add(canvas);

        // Tao scene
        scene = new Scene(root);

        // Tao timer
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                world.render(gc);
                if (connection instanceof Server) world.update(l);
                if (connection instanceof Client) world.time = l;
                if (connection != null) connection.update();
            }
        };
        timer.start();

        // Ket noi event Menu voi BombermanGame
        menuController = menuView.getController();
        menuController.createServer = this::createServer;
        menuController.createClient = this::createClient;
        menuController.startButton.setOnAction(x -> startGame());

        Portal.endGame = this::endGame;

        // Show
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (connection != null) connection.close();
    }

    public void createMap() {
        File file = new File("D:\\btl\\Bomberman\\res\\levels\\Level" + level + ".txt");
        //File file = new File("res/levels/test.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Load map error!");
            if (level > 0) level--;
            else System.exit(-1);
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
                        world.addEntity(new Brick(j, i));
                        break;
                    case 'x':
                        world.addEntity(new Brick(j, i, new Portal(j, i)));
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
                        world.addEntity(new Brick(j, i, new BombItem(j, i)));
                        break;
                    case 'f':
                        world.addEntity(new Brick(j, i, new FlameItem(j, i)));
                        break;
                    case 's':
                        world.addEntity(new Brick(j, i, new SpeedItem(j, i)));
                        break;
                }
            }
        }
    }

    public void createConnection() {
        // Chat event
        connection.receiveMessage = message -> menuController.chatView.getItems().add(message);
        menuController.chatInput.setOnAction(x -> {
            canvas.requestFocus();
            if (menuController.chatInput.getText().isEmpty()) return;
            String message = menuController.name.getText() + " : " + menuController.chatInput.getText();
            menuController.chatView.getItems().add(message);
            connection.sendMessage(message);
            menuController.chatInput.clear();
        });
        // Move event
        scene.setOnKeyPressed(x -> {
            String key = x.getCode().getName();
            if (key.equals("Enter")) menuController.chatInput.setDisable(!menuController.chatInput.isDisable());
            else connection.onKeyPressed(key);
            if (key.equals("Up") || key.equals("Down") || key.equals("Left") || key.equals("Right"))
                menuController.chatInput.setDisable(true);
        });
        scene.setOnKeyReleased(x -> connection.onKeyReleased(x.getCode().getName()));
    }

    private boolean createServer(String name) {
        try {
            connection = new Server(world, name);
            createConnection();
            return true;
        } catch (IOException e) {
            connection = null;
            return false;
        }
    }

    private boolean createClient(String host, String name) {
        try {
            connection = new Client(host, world, name);
            createConnection();
            return true;
        } catch (IOException e) {
            connection = null;
            return false;
        }
    }

    private void startGame() {
        System.gc();
        if (!(connection instanceof Server)) return;
        menuController.startButton.setDisable(true);
        Server server = (Server) connection;
        server.started = true;
        createMap();
        server.addBombers();
    }

    private void endGame(boolean isWinner) {
        System.gc();
        if (!(connection instanceof Server)) return;
        menuController.startButton.setDisable(false);
        Server server = (Server) connection;
        server.started = false;
        server.listen();
        world.entities.clear();
        world.stillObjects.clear();
        if (isWinner) level++;
    }
}

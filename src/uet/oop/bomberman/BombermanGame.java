package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import uet.oop.bomberman.container.MatrixWorld;
import uet.oop.bomberman.container.World;
import uet.oop.bomberman.display.MenuController;
import uet.oop.bomberman.entities.Portal;
import uet.oop.bomberman.entities.background.Brick;
import uet.oop.bomberman.entities.background.Grass;
import uet.oop.bomberman.entities.background.Wall;
import uet.oop.bomberman.entities.enemies.*;
import uet.oop.bomberman.entities.items.*;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.network.Client;
import uet.oop.bomberman.network.Connection;
import uet.oop.bomberman.network.Server;
import uet.oop.bomberman.sound.GameSound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class BombermanGame extends Application {

    public static int WIDTH = 0;
    public static int HEIGHT = 0;

    public static int level = 1;

    public static final World world = new MatrixWorld();

    private static Connection connection;

    private static final GameSound sounds = new GameSound();


    private MenuController menuController;

    private GraphicsContext gc;
    private Canvas canvas;

    private Scene scene;

    private Stage stage;


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
                // render
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                world.render(gc);
                // update
                if (connection instanceof Server) {
                    world.update(l);
                    // Check endGame
                    if (((Server) connection).started && world.bombers.size() == 0)
                        endGame(Portal.bomberCount > 0);
                }
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
        menuController.soundButton.setOnMouseClicked(x -> {
            menuController.soundButton.setText(sounds.changeSoundStatus() ? "Âm thanh: Bật" : "Âm thanh: Tắt");
            canvas.requestFocus();
        });

        // Show
        stage.setScene(scene);
        this.stage = stage;
        this.stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (connection != null) connection.close();
    }

    public void createMap() {

        File file = new File("res/levels/Level" + level + ".txt");

       // File file = new File("res/levels/test.txt");

        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            if (level > 1) {
                Alert alert = new Alert(Alert.AlertType.NONE);

                alert.setTitle("Winner");
                alert.setHeaderText("Game kết thúc!");
                alert.setContentText("Bạn đã chơi hết level.\nHãy đợi bản cập nhật thêm của Game!\nBạn có thể chơi lại hoặc thoát:");

                ButtonType level1 = new ButtonType("Chơi lại lv1", ButtonBar.ButtonData.APPLY);
                ButtonType levelMax = new ButtonType("Chơi lại lv" + (level - 1), ButtonBar.ButtonData.APPLY);
                ButtonType out = new ButtonType("Thoát", ButtonBar.ButtonData.APPLY);

                alert.getButtonTypes().addAll(level1, levelMax, out);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == level1) {
                    level = 1;
                    createMap();
                } else if (result.get() == levelMax) {
                    level--;
                    createMap();
                } else System.exit(0);
            } else {
                System.out.println("Load map error!");
                System.exit(-1);
            }
            return;
        }
        String info = scanner.nextLine();
        int L = Integer.parseInt(info.split(" ")[0]);
        int R = Integer.parseInt(info.split(" ")[1]);
        int C = Integer.parseInt(info.split(" ")[2]);
        try {
            reSizeMap(C, R);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
            return;
        }
        try {
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
                        case '3':
                            world.addEntity(new Doll(j, i));
                            break;
                        case '4':
                            world.addEntity(new Minvo(j, i));
                            break;
                        case '5':
                            world.addEntity(new Kondoria(j, i));
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
                        case 'h':
                            world.addEntity(new Brick(j, i, new HealthItem(j, i)));
                            break;
                        case 'W':
                            world.addEntity(new Brick(j, i, new WallPassItem(j, i)));
                            break;
                        case 'B':
                            world.addEntity(new Brick(j, i, new BombPassItem(j, i)));
                            break;
                        case 'F':
                            world.addEntity(new Brick(j, i, new FlamePassItem(j, i)));
                            break;
                    }
                }
            }
            level = L;
        } catch (Exception e) {
            System.out.println("Kích cỡ bản đồ không hợp lệ!");
            System.exit(-1);
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
            if (key.equals("Up") || key.equals("Down") || key.equals("Left") || key.equals("Right")) {
                menuController.chatInput.setDisable(true);
                canvas.requestFocus();
            }
        });
        scene.setOnKeyReleased(x -> connection.onKeyReleased(x.getCode().getName()));
        // Bomber change property event
        connection.setOnChangeBomberDisplay(menuController::update);
    }

    private boolean createServer(String name) {
        try {
            connection = new Server(world, name);
            createConnection();
            return true;
        } catch (IOException e) {
            connection = null;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Tạo server thất bại!");
            alert.show();
            return false;
        }
    }

    private boolean createClient(String host, String name) {
        try {
            connection = new Client(host, world, name);
            createConnection();
            // reSize map event
            ((Client) connection).getMap((width, height) -> {
                try {
                    reSizeMap(width, height);
                } catch (Exception ignored) {
                }
            });
            ((Client) connection).getLevel(level -> {
                BombermanGame.level = level;
                menuController.levelView.setText("Level: " + level);
            });
            // End event
            connection.endGame = this::endGame;
            return true;
        } catch (IOException e) {
            connection = null;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Không thể tham gia server");
            alert.show();
            return false;
        }
    }

    private void reSizeMap(int width, int height) throws Exception {
        if (width <= 0 || height <= 0)
            throw new Exception("Bản đồ không hợp lệ!");
        if (!(world instanceof MatrixWorld))
            throw new Exception("Không thể thay đổi bản đồ!");
        WIDTH = width;
        HEIGHT = height;
        ((MatrixWorld) world).reSize(WIDTH, HEIGHT);
        if (connection instanceof Server) ((Server) connection).setMap(WIDTH, HEIGHT);
        canvas.setWidth(WIDTH * Sprite.SCALED_SIZE);
        canvas.setHeight(HEIGHT * Sprite.SCALED_SIZE);
        stage.sizeToScene();
    }

    private void startGame() {
        System.gc();
        if (!(connection instanceof Server)) return;
        menuController.startButton.setDisable(true);
        createMap();
        Server server = (Server) connection;
        menuController.levelView.setText("Level: " + level);
        server.setLevel(level);
        server.addBombers();
        server.started = true;
    }

    private void endGame(boolean isWinner) {
        world.entities.clear();
        world.stillObjects.clear();
        sounds.stopBomberGoSound();
        if (connection instanceof Server) {
            menuController.startButton.setDisable(false);
            Server server = (Server) connection;
            server.listen();
            server.endGame.accept(isWinner);
        }
        System.gc();
        Alert alert = new Alert(Alert.AlertType.NONE);
        if (isWinner) {
            level++;
            sounds.playWinSound();
            alert.setTitle("End Game");
            alert.setHeaderText("Winner");
            alert.setContentText("You are winner.");
        } else {
            sounds.playLoseSound();
            alert.setTitle("Game Over");
            alert.setHeaderText("Loser");
            alert.setContentText("You are loser.");
        }
        alert.getButtonTypes().add(ButtonType.OK);
        alert.show();
        canvas.setWidth(0);
        canvas.setHeight(0);
        stage.sizeToScene();
        menuController.levelView.setText("Level: " + level);
    }

    public static void playSound(String name, String sound) {
        if (connection == null || name == null || sound == null) return;
        if (connection.name.equals(name) || name.equals("All")) {
            switch (sound) {
                case "bomberGo":
                    sounds.playBomberGoSound();
                    break;
                case "getExtraItems":
                    sounds.playGetExtraItemsSound();
                    break;
                case "putBomb":
                    sounds.playPutBombSound();
                    break;
                case "BombExplode":
                    sounds.playBombExplodeSound();
                    break;
            }
        }
        if (connection instanceof Server)
            if (!connection.name.equals(name))
                if (!sound.equals("bomberGo"))
                    ((Server) connection).playSound(name, sound);
    }

    public static void stopSound(String name, String sound) {
        if (connection == null || name == null || sound == null) return;
        if (!name.equals(connection.name)) return;
        switch (sound) {
            case "bomberGo":
                sounds.stopBomberGoSound();
                break;
            case "background":
                sounds.stopBGM();
        }
    }
}

package uet.oop.bomberman.display;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import uet.oop.bomberman.container.World;
import uet.oop.bomberman.network.Client;
import uet.oop.bomberman.network.Connection;
import uet.oop.bomberman.network.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MenuController implements Initializable {

    private Consumer<Connection> connectedAction;
    private Consumer<Button> startAction;
    private World world;

    @FXML
    public GridPane info;

    @FXML
    public TextField name;

    @FXML
    public TextField host;

    @FXML
    public HBox buttonContainer;

    @FXML
    public ListView chatView;

    @FXML
    public TextField chatInput;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        info.setDisable(false);
        chatView.setDisable(true);
        chatInput.setDisable(true);
        try {
            host.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
            System.out.println("Get my host error!");
        }
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setConnectedAction(Consumer<Connection> connectedAction) {
        this.connectedAction = connectedAction;
    }

    public void setStartAction(Consumer<Button> startAction) {
        this.startAction = startAction;
    }

    @FXML
    public void createServer() {
        if (name.getText().isEmpty()) return;
        Connection connection;
        try {
            if (world != null) connection = new Server(world, name.getText());
            else connection = new Server(new World(), name.getText());
            host.setText(InetAddress.getLocalHost().getHostAddress());
            info.setDisable(true);
            chatView.setDisable(false);
            chatInput.setDisable(false);
            buttonContainer.getChildren().clear();
            Button startButton = new Button("Start");
            startButton.setOnAction(x -> startAction.accept(startButton));
            buttonContainer.getChildren().add(startButton);
            connection.setListView(chatView);
            connection.setTextField(chatInput);
            connectedAction.accept(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void joinServer() {
        if (name.getText().isEmpty()) return;
        Connection connection;
        try {
            if (world != null) connection = new Client(host.getText(), world, name.getText());
            else connection = new Client(host.getText(), new World(), name.getText());
            info.setDisable(true);
            chatView.setDisable(false);
            chatInput.setDisable(false);
            buttonContainer.getChildren().clear();
            connection.setListView(chatView);
            connection.setTextField(chatInput);
            connectedAction.accept(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

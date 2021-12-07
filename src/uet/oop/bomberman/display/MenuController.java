package uet.oop.bomberman.display;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MenuController implements Initializable {

    public BiFunction<String, String, Boolean> createClient;
    public Function<String, Boolean> createServer;

    public final Button startButton = new Button("Start");

    public final Button soundButton = new Button("Âm thanh");

    @FXML
    public GridPane info;

    @FXML
    public TextField name;

    @FXML
    public TextField host;

    @FXML
    public HBox buttonContainer;

    @FXML
    public ListView<String> bomberListView;

    @FXML
    public ListView<String> chatView;

    @FXML
    public TextField chatInput;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        info.setDisable(false);
        chatView.setDisable(true);
        chatInput.setDisable(true);
        bomberListView.setFocusTraversable(false);
        chatView.setFocusTraversable(false);
        soundButton.setFocusTraversable(false);
        try {
            host.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
            System.out.println("Lấy địa chỉ IP không thành công!");
        }
    }

    @FXML
    public void onCreateServer() {
        if (name.getText().isEmpty()) return;
        try {
            host.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("Lấy địa chỉ IP không thành công!");
        }
        if (createServer.apply(name.getText())) {
            info.setDisable(true);
            chatView.setDisable(false);
            chatInput.setDisable(false);
            buttonContainer.getChildren().clear();
            buttonContainer.getChildren().add(startButton);
            buttonContainer.getChildren().add(soundButton);
        }
    }

    @FXML
    public void onJoinServer() {
        if (name.getText().isEmpty()) return;
        if (createClient.apply(host.getText(), name.getText())) {
            info.setDisable(true);
            chatView.setDisable(false);
            chatInput.setDisable(false);
            buttonContainer.getChildren().clear();
            buttonContainer.getChildren().add(soundButton);
        }
    }

    public void update(List<String> bomberDisplaysList) {
        bomberListView.getItems().clear();
        for (String display : bomberDisplaysList)
            if (display != null)
                bomberListView.getItems().add(display.replace('\t', '\n'));
    }
}

package uet.oop.bomberman.display;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    public final Button soundButton = new Button("Âm thanh: Bật");

    @FXML
    public GridPane info;

    @FXML
    public TextField name;

    @FXML
    public TextField host;

    @FXML
    public HBox buttonContainer;

    @FXML
    public Label levelView;

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

    private boolean checkName(String name) {
        if (name == null) return false;
        else if (name.isEmpty()) return false;
        else if (name.equals("All")) return false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            boolean check = (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
            if (!check) return false;
        }
        return true;
    }

    @FXML
    public void onCreateServer() {
        if (!checkName(name.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tên không hợp lệ!");
            alert.setHeaderText("Tên không hợp lệ!");
            alert.setContentText("Tên chỉ chứa kí tự: a->z, A->Z, 0->9.");
            alert.show();
            return;
        }
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
            Label temp = new Label(" ");
            temp.setMinWidth(10);
            buttonContainer.getChildren().addAll(startButton, temp, soundButton);
        }
    }

    @FXML
    public void onJoinServer() {
        if (!checkName(name.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tên không hợp lệ!");
            alert.setHeaderText("Tên không hợp lệ!");
            alert.setContentText("Tên chỉ chứa kí tự: a->z, A->Z, 0->9.");
            alert.show();
            return;
        }
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

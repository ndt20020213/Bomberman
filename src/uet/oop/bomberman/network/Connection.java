package uet.oop.bomberman.network;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import uet.oop.bomberman.container.World;

import java.io.IOException;

public abstract class Connection {

    public final String name;
    public final World world;

    private ListView listView = null;
    private TextField textField = null;

    Connection(World world, String name) {
        this.world = world;
        this.name = name;
    }

    public abstract void onKeyPressed(String key);

    public abstract void onKeyReleased(String key);

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
        textField.setOnAction(x -> {
            sendMessage(name + " : " + textField.getText());
            textField.setText("");
        });
    }

    protected void sendMessage(String message) {
        if (listView != null) listView.getItems().add(message);
    }

    protected void receiveMessage(String message) {
        if (listView != null) listView.getItems().add(message);
    }

    public abstract void close() throws IOException;
}

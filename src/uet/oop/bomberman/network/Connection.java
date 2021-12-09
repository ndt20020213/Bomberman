package uet.oop.bomberman.network;

import uet.oop.bomberman.container.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public abstract class Connection {

    public final String name;
    public final World world;

    public Consumer<Boolean> endGame = null;

    protected final HashMap<String, String> bombersDisplay = new HashMap<>();

    Connection(World world, String name) {
        this.world = world;
        this.name = name;
    }

    public abstract void update();

    public abstract void onKeyPressed(String key);

    public abstract void onKeyReleased(String key);

    protected Runnable changingBomberDisplay = () -> {};

    public void setOnChangeBomberDisplay(Consumer<List<String>> changingBomberDisplay) {
        this.changingBomberDisplay = () -> {
            ArrayList<String> status = new ArrayList<>();
            status.add(bombersDisplay.get(name));
            bombersDisplay.forEach((name, display) -> {
                if (display != null)
                    if (!name.equals(this.name))
                        status.add(display);
            });
            changingBomberDisplay.accept(status);
        };
    }

    public abstract void sendMessage(String message);

    public Consumer<String> receiveMessage = x -> {};

    public abstract void close() throws IOException;
}

package uet.oop.bomberman.network;

import uet.oop.bomberman.container.World;

import java.io.IOException;
import java.util.function.Consumer;

public abstract class Connection {

    public final String name;
    public final World world;

    Connection(World world, String name) {
        this.world = world;
        this.name = name;
    }

    public abstract void update();

    public abstract void onKeyPressed(String key);

    public abstract void onKeyReleased(String key);

    public abstract void sendMessage(String message);

    public Consumer<String> receiveMessage = x -> {};

    public abstract void close() throws IOException;
}

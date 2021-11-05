package uet.oop.bomberman.network;

import uet.oop.bomberman.container.World;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bricks.RedBrick;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server extends Connection {
    private final ServerSocket server;
    private final ArrayList<ClientSocket> clientSockets = new ArrayList<>();
    int maxKey = 0;

    public Server(World world, String name) throws IOException {
        super(world, name);
        server = new ServerSocket("BaTe".hashCode() % 5000 + 1000);
        listen();
        world.addAction = this::addEntity;
        world.removeAction = this::removeEntity;
    }

    public boolean started = false;

    public void listen() {
        started = false;
        new Thread(() -> {
            while (!started && !server.isClosed()) {
                try {
                    ClientSocket clientSocket = new ClientSocket(this, clientSockets, server.accept());
                    clientSockets.add(clientSocket);
                } catch (IOException e) {
                }
            }
        }).start();
    }

    private Bomber bomber;
    public void addBombers() {
        bomber = new Bomber(1,1);
        bomber.setName(name);
        world.addEntity(bomber);
        clientSockets.forEach(clientSocket -> {
            Bomber temp = new Bomber(1,1);
            world.addEntity(temp);
            clientSocket.setBomber(temp);
        });
    }

    @Override
    public void onKeyPressed(String key) {
        bomber.keyPressed(key);
    }

    @Override
    public void onKeyReleased(String key) {
        bomber.keyReleased(key);
    }

    @Override
    protected void sendMessage(String message) {
        super.sendMessage(message);
        clientSockets.forEach(client -> client.SendLine("Chat#" + message));
    }

    public void update() {
        String states = "";
        for (Entity entity : world.entities)
            states = states.concat("Update#" + entity.getKey() + "#" + entity + "\r\n");
        for (Entity entity : world.bricks)
            if (entity instanceof RedBrick)
                states = states.concat("Update#" + entity.getKey() + "#" + entity + "\r\n");
        for (ClientSocket clientSocket : clientSockets) clientSocket.SendLine(states);
    }

    private boolean addEntity(Entity entity) {
        entity.setKey(maxKey);
        String data = entity.toString();
        clientSockets.forEach(client -> client.SendLine("Add#" + maxKey + "#" + entity.getClass().getSimpleName() + "#" + data));
        maxKey++;
        return true;
    }

    private boolean removeEntity(Entity entity) {
        clientSockets.forEach(client -> client.SendLine("Remove#" + entity.getKey()));
        return true;
    }

    @Override
    public void close() throws IOException {
        for (ClientSocket clientSocket : clientSockets) clientSocket.close();
        server.close();
    }
}

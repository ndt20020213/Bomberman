package uet.oop.bomberman.network;

import uet.oop.bomberman.container.World;
import uet.oop.bomberman.entities.player.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bricks.RedBrick;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends Connection {
    private final ServerSocket server;
    private final ArrayList<ClientSocket> clientSockets = new ArrayList<>();
    int maxKey = 0;

    HashMap<Integer, String> statusHistory = new HashMap<>();

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
                    sendMessage("Ai đó vừa kết nối thất bại!");
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
        if (bomber != null) bomber.keyPressed(key);
    }

    @Override
    public void onKeyReleased(String key) {
        if (bomber != null) bomber.keyReleased(key);
    }

    @Override
    public void sendMessage(String message) {
        clientSockets.forEach(client -> client.SendLine("Chat#" + message));
    }

    public void update() {
        StringBuilder states = new StringBuilder();
        for (Entity entity : world.entities) {
            String temp = "Update#" + entity.getKey() + "#" + entity + "\n";
            String oldStatus = statusHistory.get(entity.getKey());
            if (oldStatus == null) {
                statusHistory.put(entity.getKey(), temp);
                states.append(temp);
            } else if (!temp.equals(oldStatus)) {
                statusHistory.put(entity.getKey(), temp);
                states.append(temp);
            }
        }
        for (Entity entity : world.bricks)
            if (entity instanceof RedBrick) {
                String temp = "Update#" + entity.getKey() + "#" + entity + "\n";
                String oldStatus = statusHistory.get(entity.getKey());
                if (oldStatus == null) {
                    statusHistory.put(entity.getKey(), temp);
                    states.append(temp);
                } else if (!temp.equals(oldStatus)) {
                    statusHistory.put(entity.getKey(), temp);
                    states.append(temp);
                }
            }
        for (ClientSocket clientSocket : clientSockets) clientSocket.SendLine(states.toString());
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

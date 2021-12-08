package uet.oop.bomberman.network;

import uet.oop.bomberman.container.World;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemies.Enemy;
import uet.oop.bomberman.entities.player.Bomber;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Server extends Connection {
    private final ServerSocket server;
    private final ArrayList<ClientSocket> clientSockets = new ArrayList<>();
    private int maxKey = 0;

    // Lịch sử trạng thái các Entity
    private final HashMap<Integer, String> statusHistory = new HashMap<>();

    // Bộ đệm lưu message
    public final LinkedList<String> messageHistory = new LinkedList<>();

    // Lưu dữ liệu gửi tới Client
    private final StringBuilder states = new StringBuilder();

    public Server(World world, String name) throws IOException {
        super(world, name);
        server = new ServerSocket("BaTe".hashCode() % 5000 + 1000);
        listen();
        updateToClient();
        world.addAction = this::addEntity;
        world.removeAction = this::removeEntity;
        endGame = isWinner -> {
            bombersDisplay.clear();
            changingBomberDisplay.run();
            clientSockets.forEach(client -> client.SendLine("End#" + isWinner));
        };
    }

    // Lắng nghe kết nối client
    public boolean started = true;

    public void listen() {
        if (!started) return;
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

    // Bomber đại diện
    private Bomber bomber;
    public void addBombers() {
        bomber = new Bomber(1,1);
        bomber.name = name;
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

    public void setMap(int w, int h) {
        if (w <= 0 || h <= 0) return;
        String command = "Map#" + w + " " + h;
        clientSockets.forEach(client -> client.SendLine(command));
    }

    public void playSound(String name, String sound) {
        String command = "Sound#" + name + '#' + sound;
        clientSockets.forEach(client -> client.SendLine(command));
    }

    // Gửi dữ liệu tới Client
    private void updateToClient() {
        new Thread(() -> {
            while (!server.isClosed()) {
                try {
                    if (states.length() > 0) {
                        String commands = states.toString();
                        for (ClientSocket clientSocket : clientSockets)
                            clientSocket.SendLine(commands);
                        states.setLength(0);
                    }
                    Thread.sleep(5);
                } catch (Exception ignored) {
                }
            }
        }).start();
    }

    @Override
    public void update() {
        // Đọc tin nhắn
        while (messageHistory.size() > 0) {
            String message = messageHistory.poll();
            if (message != null) receiveMessage.accept(message);
        }
        // Gửi trạng thái world từ server tới client
        //StringBuilder states = new StringBuilder();
        for (Entity entity : world.entities) {
            String newStatus = entity.toString();
            if (entity instanceof Enemy)
                states.append("Update#").append(entity.getKey()).append('#').append(newStatus).append('\n');
            else {
                String oldStatus = statusHistory.get(entity.getKey());
                if (!newStatus.equals(oldStatus)) {
                    statusHistory.put(entity.getKey(), newStatus);
                    states.append("Update#").append(entity.getKey()).append('#').append(newStatus).append('\n');
                }
            }
        }
        // Gửi trạng thái Bombers cho Display và Client
        boolean hasChanging = false;
        for (Bomber bomber : world.bombers) {
            String display = bomber.display();
            String oldDisplay = bombersDisplay.get(bomber.name);
            if (!display.equals(oldDisplay)) {
                hasChanging = true;
                states.append("BomberDisplay#").append(bomber.name).append('#').append(display).append('\n');
                bombersDisplay.put(bomber.name, display);
            }
        }
        if (hasChanging) changingBomberDisplay.run();
    }

    // Gửi yêu cầu thêm Entity đến client
    private boolean addEntity(Entity entity) {
        entity.setKey(maxKey);
        String data = entity.toString();
        String command = "Add#" + maxKey + "#" + entity.getClass().getSimpleName() + "#" + data;
        clientSockets.forEach(client -> client.SendLine(command));
        maxKey++;
        return true;
    }

    // Gửi yêu cầu xóa Entity đến client
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

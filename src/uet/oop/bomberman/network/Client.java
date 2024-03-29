package uet.oop.bomberman.network;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.container.World;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.player.Bomber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Client extends Connection {

    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;

    private final HashMap<Integer, IConnected> connectedEntities = new HashMap<>();

    private final LinkedList<String> commands = new LinkedList<>();

    public Client(String host, World world, String name) throws IOException {
        super(world, name);
        client = new Socket(host, "BaTe".hashCode() % 5000 + 1000);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        listen();
        sendLine("Start#" + name);
    }

    private void listen() {
        new Thread(() -> {
            while (!client.isClosed() && client.isConnected()) {
                try {
                    String line = in.readLine();
                    commands.add(line);
                } catch (Exception ignored) {
                }
            }
        }).start();
    }

    private void sendLine(String command) {
        out.println(command);
        out.flush();
    }

    private String status = "";

    @Override
    public void onKeyPressed(String key) {
        if (status.equals(key)) return;
        switch (key) {
            case "W":
            case "Up":
            case "S":
            case "Down":
            case "A":
            case "Left":
            case "D":
            case "Right":
                status = key;
                sendLine("Move#" + key);
                return;
            case "Space":
                sendLine("Move#Space");
        }
    }

    @Override
    public void onKeyReleased(String key) {
        if (status.equals(key)) {
            status = "";
            sendLine("Move#");
        }
    }

    @Override
    public void sendMessage(String message) {
        sendLine("Chat#" + message);
    }

    private Consumer<Integer> setLevel = l -> {};

    public void getLevel(Consumer<Integer> setLevel) {
        this.setLevel = setLevel;
    }

    private BiConsumer<Integer, Integer> setMap = (w, h) -> {};

    public void getMap(BiConsumer<Integer, Integer> setMap) {
        this.setMap = setMap;
    }

    @Override
    public void update() {
        while (true) {
            try {
                String command = commands.poll();
                if (command == null) break;
                String[] data = command.split("#");
                if (data.length <= 0) continue;
                switch (data[0]) {
                    case "Start":
                        world.entities.clear();
                        world.stillObjects.clear();
                        break;
                    case "End":
                        System.out.println(command);
                        if (data.length >= 2 && endGame != null)
                            endGame.accept(Boolean.parseBoolean(data[1]));
                        else System.out.println(command);
                        bombersDisplay.clear();
                        changingBomberDisplay.run();
                        break;
                    case "Chat":
                        if (data.length >= 2 && receiveMessage != null)
                            receiveMessage.accept(data[1]);
                        break;
                    case "Level":
                        if (data.length >= 2) setLevel.accept(Integer.parseInt(data[1]));
                        break;
                    case "Map":
                        if (data.length < 2) break;
                        String[] size = data[1].split(" ");
                        if (size.length < 2) break;
                        int w = Integer.parseInt(size[0]);
                        int h = Integer.parseInt(size[1]);
                        if (w > 0 && h > 0) setMap.accept(w, h);
                        break;
                    case "BomberDisplay":
                        if (data.length < 3) break;
                        bombersDisplay.put(data[1], data[2]);
                        changingBomberDisplay.run();
                        break;
                    case "Sound":
                        if (data.length == 3) BombermanGame.playSound(data[1], data[2]);
                        break;
                    case "Add":
                        Entity entity = (Entity) IConnected.getConnectedEntity(data[2], data[3]);
                        if (entity == null) break;
                        entity.setKey(Integer.parseInt(data[1]));
                        world.addEntity(entity);
                        connectedEntities.put(entity.getKey(), entity);
                        break;
                    case "Update":
                        connectedEntities.get(Integer.parseInt(data[1])).update(data[2]);
                        break;
                    case "Remove":
                        Entity removeEntity = (Entity) connectedEntities.remove(Integer.parseInt(data[1]));
                        world.removeEntity(removeEntity);
                        if (removeEntity instanceof Bomber)
                            if (((Bomber) removeEntity).name.equals(name))
                                BombermanGame.stopSound(name, "bomberGo");
                        break;
                    case "Close":
                        out.close();
                        in.close();
                        client.close();
                        System.exit(0);
                }
            } catch (Exception e) {
                break;
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (!client.isClosed()) {
            sendLine("Close#");
            out.close();
            in.close();
            client.close();
        }
    }
}

package uet.oop.bomberman.network;

import uet.oop.bomberman.container.World;
import uet.oop.bomberman.entities.Entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class Client extends Connection {

    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;

    private final HashMap<Integer, IConnected> connectedEntities = new HashMap<>();

    public Client(String host, World world, String name) throws IOException {
        super(world, name);
        client = new Socket(host, "BaTe".hashCode() % 5000 + 1000);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        listen();
        out.print("Start#" + name + "\r\n");
        out.flush();
    }

    private void listen() {
        new Thread(() -> {
            String line;
            while (!client.isClosed()) {
                try {
                    line = in.readLine();
                } catch (Exception e) {
                    continue;
                }
                String[] command = line.split("#");
                if (command.length <= 0) continue;
                switch (command[0]) {
                    case "Start":
                    case "End":
                        world.entities.clear();
                        world.stillObjects.clear();
                        break;
                    case "Chat":
                        receiveMessage.accept(command[1]);
                        break;
                    case "Add":
                        Entity entity = (Entity) IConnected.getConnectedEntity(command[2], command[3]);
                        if (entity == null) break;
                        entity.setKey(Integer.parseInt(command[1]));
                        world.addEntity(entity);
                        connectedEntities.put(entity.getKey(), entity);
                        break;
                    case "Update":
                        connectedEntities.get(Integer.parseInt(command[1])).update(command[2]);
                        break;
                    case "Delete":
                        world.removeEntity((Entity) connectedEntities.remove(Integer.parseInt(command[1])));
                        break;
                }
            }
        }).start();
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
                out.print("Move#" + key + "\r\n");
                out.flush();
                return;
            case "Space":
                out.print("Move#Space\r\n");
                out.flush();
        }
    }

    @Override
    public void onKeyReleased(String key) {
        if (status.equals(key)) {
            out.print("Move#" + "\r\n");
            out.flush();
        }
    }

    @Override
    public void sendMessage(String message) {
        out.print("Chat#" + message + "\r\n");
        out.flush();
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}

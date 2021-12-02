package uet.oop.bomberman.network;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.player.Bomber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

class ClientSocket {

    private final Server server;
    private final ArrayList<ClientSocket> clientSockets;

    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Bomber bomber;
    private String name;

    public ClientSocket(Server server, ArrayList<ClientSocket> clientSockets, Socket socket) {
        this.server = server;
        this.clientSockets = clientSockets;
        this.socket = socket;
        try {
            out = new PrintWriter(this.socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (Exception e) {
            System.out.println("new ClientSocket Error!");
            System.out.println(e.getMessage());
        }
        listen();
    }

    public void setBomber(Bomber bomber) {
        this.bomber = bomber;
        this.bomber.setName(name);
    }

    private void listen() {
        new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    String line = in.readLine();
                    String[] command = line.split("#");
                    switch (command[0]) {
                        case "Start":
                            if (command.length >= 2) name = command[1];
                            server.messageHistory.add(name + " Tham gia Server!");
                            for (ClientSocket clientSocket : clientSockets)
                                if (clientSocket != this) clientSocket.SendLine("Chat#" + name + " Tham gia Server!");
                            break;
                        case "Chat":
                            server.messageHistory.add(command[1]);
                            for (ClientSocket clientSocket : clientSockets)
                                if (clientSocket != this) clientSocket.SendLine(line);
                            break;
                        case "Move":
                            if (bomber == null) break;
                            else if (command.length < 2) bomber.keyReleased(null);
                            else bomber.keyPressed(command[1]);
                            break;
                        case "Close":
                            clientSockets.remove(this);
                            BombermanGame.world.removeEntity(bomber);
                            server.messageHistory.add(name + " đã thoát!");
                            for (ClientSocket clientSocket : clientSockets)
                                if (clientSocket != this) clientSocket.SendLine("Chat#" + name + " đã thoát!");
                            out.close();
                            in.close();
                            socket.close();
                    }
                } catch (Exception ignored) {
                }
            }
        }).start();
    }

    public void SendLine(String data) {
        out.println(data);
        out.flush();
    }

    public void close() throws IOException {
        if (!socket.isClosed()) {
            SendLine("Close#");
            out.close();
            in.close();
            socket.close();
        }
    }
}
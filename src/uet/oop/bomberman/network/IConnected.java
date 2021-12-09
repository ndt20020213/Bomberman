package uet.oop.bomberman.network;

import uet.oop.bomberman.entities.attack.Bomb;
import uet.oop.bomberman.entities.attack.Flame;
import uet.oop.bomberman.entities.attack.HFlame;
import uet.oop.bomberman.entities.attack.VFlame;
import uet.oop.bomberman.entities.background.Brick;
import uet.oop.bomberman.entities.background.Grass;
import uet.oop.bomberman.entities.Portal;
import uet.oop.bomberman.entities.background.Wall;
import uet.oop.bomberman.entities.enemies.*;
import uet.oop.bomberman.entities.items.*;
import uet.oop.bomberman.entities.player.Bomber;

public interface IConnected {

    boolean setKey(int key);

    int getKey();

    IConnected update(String string);

    String toString();

    static IConnected getConnectedEntity(String className, String data) {
        switch (className) {
            case "Bomber":
                return new Bomber(0, 0).update(data);

            case "Wall":
                return new Wall(0, 0).update(data);
            case "Grass":
                return new Grass(0, 0).update(data);
            case "Brick":
                return new Brick(-1, -1).update(data);

            case "Bomb":
                return new Bomb(new Bomber(-1, -1), 0).update(data);
            case "Flame":
                return new Flame(0, 0, 0).update(data);
            case "VFlame":
                return new VFlame(0, 0, 0).update(data);
            case "HFlame":
                return new HFlame(0, 0, 0).update(data);

            case "SpeedItem":
                return new SpeedItem(0, 0).update(data);
            case "BombItem":
                return new BombItem(0, 0).update(data);
            case "FlameItem":
                return new FlameItem(0, 0).update(data);
            case "HealthItem":
                return new HealthItem(0, 0).update(data);
            case "WallPassItem":
                return new WallPassItem(0, 0).update(data);
            case "BombPassItem":
                return new BombPassItem(0, 0).update(data);
            case "FlamePassItem":
                return new FlamePassItem(0, 0).update(data);

            case "Balloom":
                return new Balloom(0, 0).update(data);
            case "Oneal":
                return new Oneal(0, 0).update(data);
            case "Doll":
                return new Doll(0, 0).update(data);
            case "Minvo":
                return new Minvo(0, 0).update(data);
            case "Kondoria":
                return new Kondoria(0, 0).update(data);

            case "Portal":
                return new Portal(0, 0).update(data);
        }
        return null;
    }
}

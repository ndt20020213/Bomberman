package uet.oop.bomberman.network;

import uet.oop.bomberman.entities.attack.Bomb;
import uet.oop.bomberman.entities.attack.Flame;
import uet.oop.bomberman.entities.background.Grass;
import uet.oop.bomberman.entities.background.Portal;
import uet.oop.bomberman.entities.background.Wall;
import uet.oop.bomberman.entities.bricks.RedBrick;
import uet.oop.bomberman.entities.bricks.WhiteBrick;
import uet.oop.bomberman.entities.enemies.Balloon;
import uet.oop.bomberman.entities.enemies.Oneal;
import uet.oop.bomberman.entities.items.BombItem;
import uet.oop.bomberman.entities.items.FlameItem;
import uet.oop.bomberman.entities.items.SpeedItem;
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
            case "WhiteBrick":
                return new WhiteBrick(0, 0).update(data);
            case "RedBrick":
                return new RedBrick(0, 0).update(data);

            case "Bomb":
                return new Bomb(0, 0, null, 0).update(data);
            case "Flame":
                return new Flame(0, 0, 0, 0, 0, 0).update(data);

            case "SpeedItem":
                return new SpeedItem(0,0).update(data);
            case "BombItem":
                return new BombItem(0,0).update(data);
            case "FlameItem":
                return new FlameItem(0,0).update(data);

            case "Balloom":
                return new Balloon(0, 0).update(data);
            case "Oneal":
                return new Oneal(0, 0).update(data);

            case "Portal":
                return new Portal(0, 0).update(data);
        }
        return null;
    }
}

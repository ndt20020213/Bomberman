package uet.oop.bomberman.container;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.structure.Rect;

import java.util.List;

public class MatrixWorld extends World {

    public final List<Entity>[][] entitiesMatrix;

    public MatrixWorld() {
        int width = BombermanGame.WIDTH;
        int height = BombermanGame.HEIGHT;
        entitiesMatrix = new EntityList[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                entitiesMatrix[i][j] = new EntityList<>(this);
    }

    public MatrixWorld(int width, int height) {
        entitiesMatrix = new EntityList[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                entitiesMatrix[i][j] = new EntityList<>(this);
    }

    @Override
    public boolean addEntity(Entity entity) {
        if (!super.addEntity(entity)) return false;
        Rect rect = entity.getRect();
        int x = (int) rect.point.x / Sprite.SCALED_SIZE;
        int y = (int) rect.point.y / Sprite.SCALED_SIZE;
        ((EntityList<Entity>) entitiesMatrix[x][y]).data.add(entity);
        return true;
    }

    @Override
    public boolean removeEntity(Entity entity) {
        if (!super.removeEntity(entity)) return false;
        Rect rect = entity.getRect();
        int x = (int) rect.point.x / Sprite.SCALED_SIZE;
        int y = (int) rect.point.y / Sprite.SCALED_SIZE;
        ((EntityList<Entity>) entitiesMatrix[x][y]).data.remove(entity);
        return true;
    }
}

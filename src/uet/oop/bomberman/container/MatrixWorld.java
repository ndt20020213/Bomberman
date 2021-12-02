package uet.oop.bomberman.container;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.attack.Bomb;
import uet.oop.bomberman.entities.background.Wall;
import uet.oop.bomberman.entities.background.Brick;
import uet.oop.bomberman.structure.Cell;

public class MatrixWorld extends World {

    public final Entity[][] entitiesMatrix;

    public MatrixWorld() {
        int width = BombermanGame.WIDTH;
        int height = BombermanGame.HEIGHT;
        entitiesMatrix = new Entity[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                entitiesMatrix[i][j] = null;
    }

    public MatrixWorld(int width, int height) {
        entitiesMatrix = new Entity[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                entitiesMatrix[i][j] = null;
    }

    @Override
    public boolean addEntity(Entity entity) {
        if (!super.addEntity(entity)) return false;
        Cell unit = entity.getUnit();
        if (entity instanceof Wall) entitiesMatrix[unit.x][unit.y] = entity;
        if (entity instanceof Brick) entitiesMatrix[unit.x][unit.y] = entity;
        if (entity instanceof Bomb) entitiesMatrix[unit.x][unit.y] = entity;
        return true;
    }

    @Override
    public boolean removeEntity(Entity entity) {
        if (!super.removeEntity(entity)) return false;
        Cell unit = entity.getUnit();
        if (entity instanceof Wall) entitiesMatrix[unit.x][unit.y] = null;
        if (entity instanceof Brick) entitiesMatrix[unit.x][unit.y] = null;
        if (entity instanceof Bomb) entitiesMatrix[unit.x][unit.y] = null;
        return true;
    }
}

package dungeonmania.entities;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwampTile extends Entity {

    private int slowFactor;

    public SwampTile(Position position, int slowFactor) {
        super(position.asLayer(Entity.ITEM_LAYER));
        this.slowFactor = slowFactor;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {

        if (entity instanceof Player)
            return;

        if (entity instanceof Enemy) {
            Enemy enemy = (Enemy) entity;
            if (enemy.getMovementFactor() == 0) { // if just stepped on swamp tile, max out the movement_factor variable
                enemy.setMovementFactor(slowFactor);
            }
        }
    }
}

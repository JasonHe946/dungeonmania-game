package dungeonmania.entities.enemies;


import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;
    private boolean allied = false;
    private boolean adjacent = false;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
    }

    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied) return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return bribeRadius >= 0 && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }

    /**
     * bribe the merc
     */
    public void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }
    }

    public void mindControl(Player player) {
        Sceptre sceptre = player.getInventory().getFirst(Sceptre.class);

        player.use(sceptre, this);

    }

    @Override
    public void interact(Player player, Game game) {
        allied = true;
        // bribe(player);
        if (player.countEntityOfType(Sceptre.class) > 0) {
            mindControl(player);
        } else {
            bribe(player);
        }
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        Position playerPos = map.getPlayer().getPosition();
        if (allied) {
            if (adjacent) { // if adjacent, follow player
                nextPos = map.getPlayer().getPreviousPosition();
                map.moveTo(this, nextPos);
            } else { // if not use dijkstra
                nextPos = map.dijkstraPathFind(getPosition(), map.getPlayer().getPosition(), this);
                allowMove(game, getMovementFactor(), nextPos);
            }
        } else {
            // Follow hostile
            nextPos = map.dijkstraPathFind(getPosition(), map.getPlayer().getPosition(), this);
            allowMove(game, getMovementFactor(), nextPos);
        }

        // check if adjacent
        if (Position.isAdjacent(playerPos, getPosition())) {
            adjacent = true;
        }
    }


    public void setAllied(boolean allied) {
        this.allied = allied;
    }

    @Override
    public boolean isInteractable(Player player) {
        return !allied && (canBeBribed(player) || player.countEntityOfType(Sceptre.class) > 0);
    }
}

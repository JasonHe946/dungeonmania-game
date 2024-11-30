package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class EnemiesGoal implements Goal {
    private String type;
    private int target;

    public EnemiesGoal(String type) {
        this.type = type;
    }

    public EnemiesGoal(String type, int target) {
        this.type = type;
        this.target = target;
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null) return false;
        // return number of enemies who are dead (is it => target) and make sure there are no spawners
        long numSpawner = game.getMap().countEntities(ZombieToastSpawner.class);
        return numSpawner == 0 && game.getMap().getPlayer().getEnemiesDestroyed() >= target;
    }

    public String toString(Game game) {
        if (this.achieved(game)) return "";
        return ":enemies";
    }

    public String getType() {
        return type;
    }
}

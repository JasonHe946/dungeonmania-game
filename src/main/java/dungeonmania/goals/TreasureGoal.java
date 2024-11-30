package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;

public class TreasureGoal implements Goal {

    private String type;
    private int target;

    public TreasureGoal(String type) {
        this.type = type;
    }

    public TreasureGoal(String type, int target) {
        this.type = type;
        this.target = target;
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null) return false;
        int treasureCollected = game.getInitialTreasureCount() - game.getMap().getEntities(Treasure.class).size();
        int stonesCollected = game.getInitialStoneCount() - game.getMap().getEntities(SunStone.class).size();
        return treasureCollected + stonesCollected >= target;
    }

    public String toString(Game game) {
        if (this.achieved(game)) return "";
        return ":treasure";
    }

    public String getType() {
        return type;
    }
}

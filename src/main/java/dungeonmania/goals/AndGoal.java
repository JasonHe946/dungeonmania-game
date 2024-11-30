package dungeonmania.goals;

import dungeonmania.Game;

public class AndGoal implements Goal {

    private String type;
    private Goal goal1;
    private Goal goal2;

    public AndGoal(String type) {
        this.type = type;
    }

    public AndGoal(String type, Goal goal1, Goal goal2) {
        this.type = type;
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null) return false;
        return goal1.achieved(game) && goal2.achieved(game);
    }

    public String toString(Game game) {
        if (this.achieved(game)) return "";
        return "(" + goal1.toString(game) + " AND " + goal2.toString(game) + ")";
    }

    public String getType() {
        return type;
    }
}

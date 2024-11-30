package dungeonmania.goals;

import dungeonmania.Game;

public class OrGoal implements Goal {
    private String type;
    private int target;
    private Goal goal1;
    private Goal goal2;

    public OrGoal(String type) {
        this.type = type;
    }

    public OrGoal(String type, int target) {
        this.type = type;
        this.target = target;
    }

    public OrGoal(String type, Goal goal1, Goal goal2) {
        this.type = type;
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null) return false;
        return goal1.achieved(game) || goal2.achieved(game);
    }

    public String toString(Game game) {
        if (this.achieved(game)) return "";
        if (achieved(game)) return "";
        else return "(" + goal1.toString(game) + " OR " + goal2.toString(game) + ")";
    }

    public String getType() {
        return type;
    }

    public int getTarget() {
        return target;
    }
}

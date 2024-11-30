package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.enemies.Mercenary;

public class Sceptre extends Buildable {
    private int duration;
    private int ticksLeft = duration;

    public Sceptre(int duration) {
        super(null);
        this.duration = duration;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return origin;
    }

    @Override
    public void use(Game game) {
        return;
    }

    public void use(Game game, Mercenary mercenary) {
        ticksLeft--;
        if (ticksLeft <= 0) {
            game.getPlayer().remove(this);
            mercenary.setAllied(false);
        }
        return;
    }

    @Override
    public int getDurability() {
        return 1;
    }

    public int getDuration() {
        return duration;
    }

    public int getTicksLeft() {
        return ticksLeft;
    }

}

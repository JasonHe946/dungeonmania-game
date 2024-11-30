package dungeonmania.entities.enemies;

import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class Assassin extends Mercenary {

    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_HEALTH = 10.0;
    public static final double DEFAULT_ATTACK = 6.0;
    public static final double DEFAULT_FAIL_RATE = 0.0;
    private double failRate;
    private Random random = new Random(2);

    public Assassin(Position position, double health, double attack,
        int bribeAmount, int bribeRadius, double failRate) {
        super(position, health, attack, bribeAmount, bribeRadius);
        this.failRate = failRate;
    }

    @Override
    public void interact(Player player, Game game) {
        bribe(player);
        if (!(random.nextInt(100) < failRate * 100)) {
            setAllied(true);
        }
    }
}

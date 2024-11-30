package dungeonmania.entities.playerState;

import dungeonmania.entities.Player;

public class PlayerState {
    private Player player;
    private State baseState;
    private State invincibleState;
    private State invisibleState;
    private State state;

    public PlayerState(Player player) {
        this.player = player;
        baseState = new BaseState(this);
        invincibleState = new InvincibleState(this);
        invisibleState = new InvisibleState(this);
        state = baseState;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public State getInvisibleState() {
        return invisibleState;
    }

    public State getInvincibleState() {
        return invincibleState;
    }

    public State getBaseState() {
        return baseState;
    }

    public Player getPlayer() {
        return player;
    }

}

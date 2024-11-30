package dungeonmania.entities.playerState;


public class InvincibleState implements State {
    private PlayerState playerState;

    public InvincibleState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public boolean isInvisible() {
        return false;
    }

    public boolean isInvincible() {
        return true;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }
}

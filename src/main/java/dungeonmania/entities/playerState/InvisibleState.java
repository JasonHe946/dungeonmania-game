package dungeonmania.entities.playerState;

public class InvisibleState implements State {
    private PlayerState playerState;

    public InvisibleState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public boolean isInvisible() {
        return true;
    }

    public boolean isInvincible() {
        return false;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }
}

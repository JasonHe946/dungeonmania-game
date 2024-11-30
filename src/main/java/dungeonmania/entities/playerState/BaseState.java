package dungeonmania.entities.playerState;

public class BaseState implements State {
    private PlayerState playerState;

    public BaseState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public boolean isInvisible() {
        return false;
    }

    public boolean isInvincible() {
        return false;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

}

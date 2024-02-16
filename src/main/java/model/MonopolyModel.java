package model;

public final class MonopolyModel {

    private GameState gameState;

    public void update() {
        if (gameState == null) {
            return;
        }
        gameState.update();
    }

    public void setGameState(final GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}

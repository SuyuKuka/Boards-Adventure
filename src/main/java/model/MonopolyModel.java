package model;

public final class MonopolyModel {

    private GameData gameState;

    public void update() {
        if (gameState == null) {
            return;
        }
        gameState.update();
    }

    public void setGameState(final GameData gameState) {
        this.gameState = gameState;
    }

    public GameData getGameState() {
        return gameState;
    }
}

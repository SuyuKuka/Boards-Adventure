package model;

public final class MonopolyModel {

    private GameLogic gameState;

    public void update() {
        if (gameState == null) {
            return;
        }
        gameState.update();
    }

    public void setGameState(final GameLogic gameState) {
        this.gameState = gameState;
    }

    public GameLogic getGameState() {
        return gameState;
    }
}

package model;

public final class MonopolyModel {

    private GameLogic gameLogic;

    public void update() {
        if (gameLogic == null) {
            return;
        }
        gameLogic.update();
    }

    public void setGameLogic(final GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }
}

package game;

public interface GameState {
    void gameStart();
    void newTurn();
    void inJail();
    void rollDice();
    void playerMove();
    void askIntersection();
    void playerArrived();
    void askProperty();
    void askShop();
    void endTurn();
    void gameEnd();
}

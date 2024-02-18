package controller;

import model.GameState;
import model.MonopolyModel;
import model.config.GameMap;
import view.MonopolyView;

public final class MonopolyController {

    private MonopolyModel model;
    private MonopolyView view;

    public MonopolyController() { }

    public void startGame(final GameMap gameMap) {
        model.setGameState(new GameState(gameMap));
    }

    public void setModel(final MonopolyModel model) {
        this.model = model;
    }

    public void setView(final MonopolyView view) {
        this.view = view;
    }

}

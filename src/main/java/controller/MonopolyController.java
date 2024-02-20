package controller;

import java.util.ArrayList;

import model.GameState;
import model.MonopolyModel;
import model.config.GameMap;
import model.config.players.HumanPlayer;
import view.MonopolyView;

public final class MonopolyController {

    private MonopolyModel model;
    private MonopolyView view;

    public MonopolyController() { }

    public void startGame(final GameMap gameMap) {
        final ArrayList<HumanPlayer> humanPlayers = new ArrayList<>();
        final int tmpHumanPlayerNB = 4;
        for (int i = 1; i <= tmpHumanPlayerNB; i++) {
            humanPlayers.add(new HumanPlayer(tmpHumanPlayerNB, "joueur" + i));
        }
        model.setGameState(new GameState(gameMap, 35, humanPlayers, 0));
    }

    public void setModel(final MonopolyModel model) {
        this.model = model;
    }

    public void setView(final MonopolyView view) {
        this.view = view;
    }

}

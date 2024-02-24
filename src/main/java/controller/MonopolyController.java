package controller;

import java.util.ArrayList;

import model.GameLogic;
import model.MonopolyModel;
import model.config.GameMap;
import model.config.Team;
import model.config.players.ComputerPlayer;
import model.config.players.HumanPlayer;
import model.config.players.Player;
import view.MonopolyView;

public final class MonopolyController {

    private MonopolyModel model;
    private MonopolyView view;

    public MonopolyController() { }

    public void startGame(final GameMap gameMap) {
        final ArrayList<Player> players = new ArrayList<>();
        final int tmpHumanPlayerNB = 4;
        final int tmpComputerPlayerNB = 0;
        for (int i = 1; i <= tmpHumanPlayerNB; i++) {
            players.add(new HumanPlayer(tmpHumanPlayerNB, "joueur" + i, new Team(i, "team" + i)));
        }
        for (int i = 1 + tmpHumanPlayerNB; i <= 1 + tmpHumanPlayerNB + tmpComputerPlayerNB; i++) {
            players.add(new ComputerPlayer(tmpComputerPlayerNB, "ordi" + i, new Team(i, "team" + i)));
        }
        model.setGameState(new GameLogic(gameMap, 35, players));
    }

    public void setModel(final MonopolyModel model) {
        this.model = model;
    }

    public void setView(final MonopolyView view) {
        this.view = view;
    }

}

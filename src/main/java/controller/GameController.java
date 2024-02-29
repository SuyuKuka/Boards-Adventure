package controller;

import java.util.ArrayList;

import game.Launcher.Mode;
import model.GameLogic;
import model.GameModel;
import model.config.GameMap;
import model.config.Team;
import model.config.players.ComputerPlayer;
import model.config.players.HumanPlayer;
import model.config.players.Player;
import view.GameView;

public final class GameController {

    private GameModel model;
    private GameView view;

    private Mode mode = Mode.GAME;

    public GameController() { }

    public void startGame(final GameMap gameMap) {
        final ArrayList<Player> players = new ArrayList<>();
        final int tmpHumanPlayerNB = 4;
        final int tmpComputerPlayerNB = 0;
        for (int i = 1; i <= tmpHumanPlayerNB; i++) {
            players.add(new HumanPlayer(tmpHumanPlayerNB, "joueur" + i, new Team(i, "team" + i)));
        }
        for (int i = 1 + tmpHumanPlayerNB; i < 1 + tmpHumanPlayerNB + tmpComputerPlayerNB; i++) {
            players.add(new ComputerPlayer(tmpComputerPlayerNB, "ordi" + i, new Team(i, "team" + i)));
        }
        model.setGameLogic(new GameLogic(gameMap, 35, players));
    }

    public void setModel(final GameModel model) {
        this.model = model;
    }

    public void setView(final GameView view) {
        this.view = view;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(final Mode mode) {
        this.mode = mode;
    }

}

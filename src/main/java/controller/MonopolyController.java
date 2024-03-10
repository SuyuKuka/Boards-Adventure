package controller;

import java.util.ArrayList;
import java.util.HashMap;

import model.GameLogic;
import model.MonopolyModel;
import model.config.GameMap;
import model.config.Item;
import model.config.Team;
import model.config.players.ComputerPlayer;
import model.config.players.HumanPlayer;
import model.config.players.Player;

import view.MonopolyView;

public final class MonopolyController {

    private MonopolyModel model;
    private MonopolyView view;
    private HashMap<String, Integer> gameConfig;

    private Team team;
    private Player player;
    private Item item;

    private final int maxPlayers = 4;
    private int nbPlayers = 0;

    public MonopolyController() { }

    public void startGame(final HashMap<String, Integer> config) {
        final ArrayList<Player> players = new ArrayList<>();
        gameConfig = config;
        // System.out.println(gameConfig.get("map"));
        // GameMap gameMap = Resources.getGameMapGraphics("map" +
        // gameConfig.get("map")).getGameMap();
        GameMap gameMap = Resources.getGameMapGraphics("map1").getGameMap();
        int nbBot = maxPlayers - nbPlayers;
        for (int i = 1; i <= nbPlayers; i++) {
            players.add(new HumanPlayer(nbPlayers, "joueur" + i, new Team(i, "team" + i)));
        }
        for (int i = 1 + nbPlayers; i < 1 + nbPlayers + nbBot; i++) {
            players.add(new ComputerPlayer(nbBot, "ordi" + i, new Team(i, "team" + i)));
        }
        // model.setGameLogic(new GameLogic(gameMap, gameConfig.get("round"), players));
        model.setGameLogic(new GameLogic(gameMap, 30, players));
    }

    public void setModel(final MonopolyModel model) {
        this.model = model;
    }

    public void setView(final MonopolyView view) {
        this.view = view;
    }

    public HashMap<String, Integer> getGameConfig() {
        return gameConfig;
    }
}

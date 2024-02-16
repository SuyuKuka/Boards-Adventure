package model;

import java.util.ArrayList;
import java.util.Random;

import model.config.GameMap;
import model.config.players.Player;
import model.config.players.Players;
import model.config.role.Farmer;

public final class GameState {
    private static final Random RAND = new Random();
    private GameMap gameMap;
    private static final int NB_PLAYERS = 4;
    private int nextPlayerID = 1;
    private ArrayList<Players> players = new ArrayList<>();

    public GameState(final GameMap gameMap) {
        this.gameMap = gameMap;
        for (int i = 0; i < NB_PLAYERS; i++) {
            players.add(new Player(nextPlayerID, "j" + nextPlayerID, new Farmer()));
            nextPlayerID++;
        }
        System.out.println(players);
    }

    public void update() {
        for (Players player : players) {
            System.out.println("==== Tour de " + player.getUsername() + " ====");
            System.out.println(player);
            System.console().readLine("Appuyez sur la touche Entrée pour jouer votre dé : ");
            player.updatePosition(RAND.nextInt(6) + 1, gameMap.maxCells());
            gameMap.getCell(player.getPosition()).play(player);
            System.out.println(player);
            System.console().readLine("Appuyez sur la touche Entrée pour valider votre tour : ");
        }
    }

    public GameMap getGameMap() {
        return gameMap;
    }
}

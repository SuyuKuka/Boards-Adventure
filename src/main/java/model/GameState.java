package model;

import java.util.ArrayList;

import model.config.GameMap;
import model.config.players.HumanPlayer;
import model.config.players.Player;
import model.config.players.PlayerState;

public final class GameState {

    private static final int NB_PLAYERS = 4;
    private final GameMap gameMap;
    private Player currentKing = null;
    private int nextPlayerID = 1;
    private ArrayList<Player> players = new ArrayList<>();

    public GameState(final GameMap gameMap) {
        this.gameMap = gameMap;
        for (int i = 0; i < NB_PLAYERS; i++) {
            players.add(new HumanPlayer(nextPlayerID, "j" + nextPlayerID));
            nextPlayerID++;
        }
        System.out.println(players);
    }

    public void update() {
        for (Player player : players) {
            System.out.println("==== Tour de " + player.getUsername() + " ====");
            System.out.println(player);
            System.console().readLine("Appuyez sur la touche Entrée pour jouer votre dé : ");
            updatePosition(player);
            playCellEffect(player);
            System.out.println(player);
            System.console().readLine("Appuyez sur la touche Entrée pour valider votre tour : ");
        }
    }

    public void updatePosition(final Player player) {
        final PlayerState playerState = player.getPlayerState();
        if (playerState == PlayerState.NORMAL) {
            player.playDice();
            if (player.getPosition() >= gameMap.getWalkableCells().size()) {
                player.incrementPosition(-gameMap.getWalkableCells().size());
                player.incrementMoney(gameMap.getLapGain());
            }
        } else if (playerState == PlayerState.JAILED) {
            player.incrementJailTurn(-1);
        } else if (playerState == PlayerState.LEADING) {

        }
    }

    public void changeKing(final Player player) {
        currentKing = player;
    }

    public void playCellEffect(final Player player) {
        gameMap.getCell(player.getPosition()).playEffect(player, this);
    }

    public Player getCurrentKing() {
        return currentKing;
    }

    public GameMap getGameMap() {
        return gameMap;
    }
}

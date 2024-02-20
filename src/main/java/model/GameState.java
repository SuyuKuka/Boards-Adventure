package model;

import java.util.ArrayList;

import model.config.GameMap;
import model.config.players.ComputerPlayer;
import model.config.players.HumanPlayer;
import model.config.players.Player;
import model.config.players.PlayerState;

public final class GameState {

    private final GameMap gameMap;
    private final int nbTurn;
    private Player currentKing = null;
    private int nextPlayerID = 1;
    private ArrayList<Player> players = new ArrayList<>();

    /**
     * Constructeur du GameState.
     * @param gameMap
     * @param nbTurn
     * @param humanPlayers
     * @param nbComputerPlayer
     */
    public GameState(final GameMap gameMap, final int nbTurn,
        final ArrayList<HumanPlayer> humanPlayers, final int nbComputerPlayer) {
        this.gameMap = gameMap;
        this.nbTurn = nbTurn;
        players.addAll(humanPlayers);
        nextPlayerID += players.size();
        for (int i = 0; i < nbComputerPlayer; i++) {
            players.add(new ComputerPlayer(nextPlayerID, "computer" + nextPlayerID));
            nextPlayerID++;
        }
        System.out.println(players);
    }

    public void update() {
        for (int i = 0; i < nbTurn; i++) {
            for (Player player : players) {
                System.out.println("==== Tour de " + player.getUsername() + " ====");
                System.out.println(player);
                System.console().readLine("Appuyez sur la touche Entrée pour jouer votre dé : ");
                if (updatePosition(player)) {
                    playCellEffect(player);
                }
                System.out.println(player);
                System.console().readLine("Appuyez sur la touche Entrée pour valider votre tour : ");
            }
        }
    }

    public boolean updatePosition(final Player player) {
        final PlayerState playerState = player.getPlayerState();
        if (playerState == PlayerState.NORMAL) {
            player.playDice();
            player.playDice();
            if (player.getPosition() >= gameMap.getWalkableCells().size()) {
                player.incrementMapLap(1);
                player.incrementPosition(-gameMap.getWalkableCells().size());
                player.incrementMoney(gameMap.getLapGain());
            }
        } else if (playerState == PlayerState.JAILED) {
            player.incrementJailTurn(-1);
            if (player.getJailTurn() <= 0) {
                player.setPlayerState(PlayerState.NORMAL);
                player.setPosition(player.getPreviousPosition());
                player.setPreviousPosition(0);
                return false;
            }
        } else if (playerState == PlayerState.LEADING) {
            player.incrementLeaderTurn(1);
        }
        return true;
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

package model;

import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;

import model.config.GameMap;
import model.config.cells.Cell;
import model.config.cells.Intersection;
import model.config.players.Player;

public final class Model {
    private static final Random RAND = new Random();

    private final GameMap gameMap;

    private final int totalTurns;
    private int currentTurn = 1;

    private Player currentLeader = null;
    private int playerListIterator = 0;
    private ArrayList<Player> players;

    /**
     * Constructeur du GameState.
     * @param gameMap
     * @param gameSettings
     * @param players
     */
    public Model(final GameMap gameMap, final HashMap<String, Integer> gameSettings,
        final ArrayList<Player> players) {
        this.gameMap = gameMap;
        this.totalTurns = gameSettings.get("turns");
        this.players = players;
    }

    public int playDice() {
        final Player currentPlayer = players.get(playerListIterator);
        int diceValue = currentPlayer.rollDice();
        Cell nextCell;
        for (int i = diceValue; i > 0; i--) {
            nextCell = gameMap.getCell(gameMap.getCell(currentPlayer.getPosition()).getNextCellID());
            if (nextCell instanceof Intersection) {
                // TODO INTERSECTION CHOIX
                final ArrayList<Integer> nextCellIDs = ((Intersection) nextCell).getNextCellIDs();
                currentPlayer.setPosition(nextCellIDs.get(RAND.nextInt(nextCellIDs.size())));
            } else {
                currentPlayer.setPosition(nextCell.getId());
            }
        }
        return 0;
    }

    public void nextPlayerTurn() {
        playerListIterator = (playerListIterator + 1) % players.size();
    }

    public void changeLeader(final Player player) {
        currentLeader = player;
    }

    public void playCellEffect(final Player player) {
        gameMap.getCell(player.getPosition()).playEffect(player, this);
    }

    public Player getCurrentLeader() {
        return currentLeader;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public Player getCurrentPlayer() {
        return players.get(playerListIterator);
    }
}

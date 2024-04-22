package model;

import java.util.ArrayList;
import java.util.HashMap;

import game.GameState;
import model.config.GameMap;
import model.config.cells.Cell;
import model.config.cells.Intersection;
import model.config.players.Player;

/**
 * Classe qui s'occupe de la logique du jeu.
 */
public final class Model {

    private final GameMap gameMap;

    private final int totalTurns;
    private int currentTurn = 1;

    /** Variable qui indique l'état actuel du jeu. {@link GameState}. */
    private GameState gameState = null;

    private Player currentLeader = null;
    private Player currentPlayer = null;

    private final ArrayList<Player> players;
    private int playerListIterator = 0;

    /**
     * Constructeur du GameState.
     * @param gameMap
     * @param gameSettings
     * @param players
     */
    public Model(final GameMap gameMap, final HashMap<String, Integer> gameSettings, final ArrayList<Player> players) {
        this.gameMap = gameMap;
        this.totalTurns = gameSettings.get("turns");
        this.players = players;
        for (Player player : players) {
            player.setPosition(gameMap.getStartCell().getId());
        }
    }

    public void newTurn() {
        currentPlayer = players.get(playerListIterator);
    }

    public void rollDice() {
        currentPlayer.rollDice();
    }

    // region playerMove

    public void playerMove() {
        Cell playerCell = currentPlayer.getPlayerCell(gameMap);
        while (currentPlayer.getDiceRollValue() > 0) {
            currentPlayer.setPosition(playerCell.getNextCellID());
            playerCell = currentPlayer.getPlayerCell(gameMap);
            currentPlayer.incrementDiceRollValue(-1);
            if (playerCell instanceof Intersection) {
                return;
            } else if (playerCell.shouldPlayerStop()) {
                currentPlayer.setDiceRollValue(0);
                return;
            }
        }
    }

    // endregion

    public void playerArrived() {
        gameMap.getCell(currentPlayer.getPosition()).playCellEffect(currentPlayer, this);
    }

    public void endTurn() {
        playerListIterator = (playerListIterator + 1) % players.size();
        if (playerListIterator == 0) {
            currentTurn++;
        }
    }

    public void changeLeader(final Player player) {
        currentLeader = player;
    }

    public void playCellEffect(final Player player) {
        gameMap.getCell(player.getPosition()).playCellEffect(player, this);
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

    public void setGameState(final GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
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

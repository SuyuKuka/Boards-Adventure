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
public final class Model implements GameState {

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
    }

    public void gameStart() {
        return;
    }

    public void newTurn() {
        System.out.println("iterator" + playerListIterator);
        currentPlayer = players.get(playerListIterator);
        System.out.println(currentPlayer.getUsername());
    }

    public void inJail() {
        return;
    }

    public void rollDice() {
        return;
    }

    public void playDice(final String diceName) {
        currentPlayer.rollDice(diceName);
    }

    public void playerMove() {
        Cell playerCell = currentPlayer.getPlayerCell(gameMap);
        while (currentPlayer.getDiceRollValue() > 0) {
            currentPlayer.setPosition(playerCell.getNextCellID());
            playerCell = currentPlayer.getPlayerCell(gameMap);
            currentPlayer.incrementDiceRollValue(-1);
            if (playerCell instanceof Intersection) {
                return;
            }
        }
    }

    public void askIntersection() {
        return;
    }

    public void playerArrived() {
        gameMap.getCell(currentPlayer.getPosition()).playCellEffect(currentPlayer, this);
    }

    public void askProperty() {
        return;
    }

    public void askShop() {
        return;
    }

    public void endTurn() {
        playerListIterator = (playerListIterator + 1) % players.size();
        if (playerListIterator == 0) {
            currentTurn++;
        }
    }

    public void gameEnd() {
        return;
    }

    // public void update() {
    //     if (gameState == null) {
    //         gameState = GameState.GAME_START;
    //         return;
    //     }
    //     switch (gameState) {
    //         case GAME_START:
    //             currentPlayer = players.get(playerListIterator);
    //             gameState = GameState.NEW_TURN;
    //             break;
    //         case NEW_TURN:
    //             if (currentPlayer.isJailed()) {
    //                 gameState = GameState.IN_JAIL;
    //             } else {
    //                 gameState = GameState.ROLL_DICE;
    //             }
    //             break;
    //         case IN_JAIL:
    //             gameState = GameState.ROLL_DICE;
    //             break;
    //         case ROLL_DICE:
    //             gameState = GameState.PLAYER_MOVE;
    //             break;
    //         case PLAYER_MOVE: {
    //             Cell playerCell = currentPlayer.getPlayerCell(gameMap);
    //             while (currentPlayer.getDiceRollValue() > 0) {
    //                 currentPlayer.setPosition(playerCell.getNextCellID());
    //                 playerCell = currentPlayer.getPlayerCell(gameMap);
    //                 if (playerCell instanceof Intersection) {
    //                     gameState = GameState.ASK_INTERSECTION;
    //                     return;
    //                 } else {
    //                     currentPlayer.incrementDiceRollValue(-1);
    //                 }
    //             }
    //             gameState = GameState.PLAYER_ARRIVED;
    //         }
    //             break;
    //         case ASK_INTERSECTION:
    //             currentPlayer.setPosition(currentPlayer.getIntersectionCellID());
    //             currentPlayer.setIntersectionCellID(0);
    //             currentPlayer.incrementDiceRollValue(-1);
    //             gameState = GameState.PLAYER_MOVE;
    //             break;
    //         case PLAYER_ARRIVED : {
    //             final Cell playerCell = currentPlayer.getPlayerCell(gameMap);
    //             playerCell.playCellEffect(currentPlayer, this);
    //             // playCellEffect s'occupe de changer de GameState.
    //         }
    //             break;
    //         case START:
    //         case CARD:
    //         case DANGER:
    //         case GAIN:
    //         case LOSS:
    //         case KINGDOM:
    //         case VOID:
    //         case TELEPORTATION:
    //         case ASK_PROPERTY:
    //         case ASK_SHOP:
    //             gameState = GameState.END_TURN;
    //             break;
    //         case END_TURN:
    //             if (currentTurn > totalTurns && playerListIterator == players.size() - 1) {
    //                 gameState = GameState.GAME_END;
    //             } else {
    //                 gameState = GameState.NEW_TURN;
    //                 playerListIterator = (playerListIterator + 1) % players.size();
    //                 if (playerListIterator == 0) {
    //                     currentTurn++;
    //                     if (currentLeader != null) {
    //                         currentLeader.getTeam().incrementGamePoint(1);
    //                     }
    //                 }
    //                 currentPlayer = players.get(playerListIterator);
    //             }
    //             break;
    //         default:
    //             break;
    //     }
    // }

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

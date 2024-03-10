package model;

import static game.Game.RAND;

import java.util.ArrayList;
import java.util.HashMap;

import model.config.GameMap;
import model.config.cells.Cell;
import model.config.cells.Intersection;
import model.config.cells.Property;
import model.config.cells.Shop;
import model.config.players.Player;

/**
 * Classe qui s'occupe de la logique du jeu.
 */
public final class Model {

    private final GameMap gameMap;

    private final int totalTurns;
    private int currentTurn;

    private GameState gameState;


    private Player currentLeader;
    private Player currentPlayer;
    private int playerListIterator;
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
        initValues();
    }

    private void initValues() {
        currentTurn = 1;
        gameState = GameState.GAME_START;
        currentLeader = null;
        currentPlayer = null;
        playerListIterator = 0;
    }

    /**
     * Méthode principale du model qui met à jour l'état du jeu et
     * met en les joueurs {@link Player} avec la map {@link GameMap}.
     */
    public void update() {
        switch (gameState) {
            case GAME_START:
                currentPlayer = players.get(playerListIterator);
                gameState = GameState.NEW_TURN;
                break;
            case NEW_TURN:
                if (currentPlayer.isJailed()) {
                    gameState = GameState.IN_JAIL;
                } else {
                    gameState = GameState.ROLL_DICE;
                }
                break;
            case IN_JAIL:
                gameState = GameState.ROLL_DICE;
                break;
            case ROLL_DICE:
                currentPlayer.rollDice();
                if (currentPlayer.isJailed() && currentPlayer.getDiceRollValue() == 6) {
                    currentPlayer.setJailTurn(0);
                    gameState = GameState.END_TURN;
                    return;
                }
                gameState = GameState.PLAYER_MOVE;
                break;
            case PLAYER_MOVE:
                Cell playerCell = currentPlayer.getPlayerCell(gameMap);
                while (currentPlayer.getDiceRollValue() > 0) {
                    currentPlayer.setPosition(playerCell.getNextCellID());
                    playerCell = currentPlayer.getPlayerCell(gameMap);
                    if (playerCell instanceof Intersection) {
                        gameState = GameState.ASK_INTERSECTION;
                        return;
                    } else {
                        currentPlayer.incrementDiceRollValue(-1);
                    }
                }
                if (playerCell instanceof Shop) {
                    gameState = GameState.ASK_SHOP;
                } else if (playerCell instanceof Property) {
                    gameState = GameState.ASK_PROPERTY;
                } else {
                    playerCell.playCellEffect(currentPlayer, this);
                    gameState = GameState.NORMAL_CELL;
                }
                break;
            case ASK_INTERSECTION:
                ArrayList<Integer> nextCellIDs = ((Intersection) currentPlayer.getPlayerCell(gameMap)).getNextCellIDs();
                currentPlayer.setPosition(nextCellIDs.get(RAND.nextInt(nextCellIDs.size())));
                currentPlayer.incrementDiceRollValue(-1);
                gameState = GameState.PLAYER_MOVE;
                break;
            case ASK_PROPERTY:
            case ASK_SHOP:
            case NORMAL_CELL:
                gameState = GameState.END_TURN;
                break;
            case END_TURN:
                if (currentTurn > totalTurns && playerListIterator == players.size() - 1) {
                    gameState = GameState.GAME_END;
                } else {
                    gameState = GameState.NEW_TURN;
                    playerListIterator = (playerListIterator + 1) % players.size();
                    if (playerListIterator == 0) {
                        currentTurn++;
                        if (currentLeader != null) {
                            currentLeader.getTeam().incrementGamePoint(1);
                        }
                    }
                    currentPlayer = players.get(playerListIterator);
                }
                break;
            default:
                break;
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

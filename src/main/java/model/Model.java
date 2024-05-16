package model;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import game.GameState;
import model.config.GameMap;
import model.config.Team;
import model.config.cells.Cell;
import model.config.players.Player;

/**
 * Classe qui s'occupe de la logique du jeu.
 */
public final class Model {

    private final GameMap gameMap;

    private final ArrayList<Player> players;
    private final HashMap<String, Team> teams;
    private int playerPlayIterator = 0;

    private final int totalTurns;
    private int currentTurn = 1;

    private GameState gameState = null;

    private Player currentLeader = null;
    private Player currentPlayer = null;


    public Model(final GameMap gameMap, final ArrayList<Player> players, final HashMap<String, String> gameConfig,
        final JSONObject mapConfig) {
        Player.resetPlayerIDIterator();
        Player.setItemShopNb(mapConfig.optInt("item_shop_nb", 1));
        this.gameMap = gameMap;
        this.totalTurns = Integer.valueOf(gameConfig.get("totalTurns"));
        this.players = players;
        this.teams = createTeams();
        currentPlayer = players.get(playerPlayIterator);
    }

    private HashMap<String, Team> createTeams() {
        final HashMap<String, Team> teamsMap = new HashMap<>();
        final HashMap<String, ArrayList<Integer>> teamIDs = new HashMap<>();
        players.forEach(player -> {
            final String hexTeamColor = player.getHexTeamColor();
            if (!teamIDs.containsKey(hexTeamColor)) {
                teamIDs.put(hexTeamColor, new ArrayList<>());
            }
            teamIDs.get(hexTeamColor).add(player.getID());
        });
        for (String hexTeamColor : teamIDs.keySet()) {
            teamsMap.put(hexTeamColor, new Team(hexTeamColor, teamIDs.get(hexTeamColor)));
        }
        return teamsMap;
    }

    public void newTurn() {
        currentPlayer = players.get(playerPlayIterator);
    }

    public void rollDice() {
        currentPlayer.rollDice();
    }

    // region playerMove

    public void playerMove() {
        Cell playerCell = currentPlayer.getPlayerCell(this);
        while (currentPlayer.getDiceRollValue() > 0) {
            currentPlayer.setPosition(playerCell.getNextCellID());
            playerCell = currentPlayer.getPlayerCell(this);
            currentPlayer.incrementDiceRollValue(-1);
            if (playerCell.shouldPlayerStopBy()) {
                return;
            }
        }
    }

    // endregion

    public void playerArrived() {
        gameMap.getCell(currentPlayer.getPosition()).playCellEffect(currentPlayer, this);
    }

    public void endTurn() {
        currentPlayer.decrementStartItemCooldown();
        playerPlayIterator = (playerPlayIterator + 1) % players.size();
        if (playerPlayIterator == 0) {
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
        return players.get(playerPlayIterator);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

}

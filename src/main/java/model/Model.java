package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;

import org.json.JSONObject;

import game.GameState;
import model.config.GameMap;
import model.config.cells.Cell;
import model.config.cells.None;

/**
 * Classe qui s'occupe de la logique du jeu.
 */
public final class Model {

    private static final Comparator<Player> RANK_COMPARATOR = createRankComparator();
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
        final Cell nextCell;
        if (currentPlayer.getNextCellID() != -1) {
            nextCell = getCell(currentPlayer.getNextCellID());
            currentPlayer.setNextCellID(-1);
        } else {
            nextCell = getCell(getCell(currentPlayer.getPosition()).getNextCellID());
        }
        currentPlayer.setPosition(nextCell.getId());
        if (!(nextCell instanceof None || nextCell.shouldPlayerStopBy())) {
            currentPlayer.incrementDiceRollValue(-1);
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

    public void updatePlayersRank() {
        final ArrayList<Player> playersRankList = new ArrayList<>(players);
        playersRankList.sort(RANK_COMPARATOR);
        int currentRank = 1;
        players.get(0).setRank(currentRank);
        for (int i = 1; i < playersRankList.size(); i++) {
            if (RANK_COMPARATOR.compare(playersRankList.get(i), playersRankList.get(i - 1)) != 0) {
                currentRank++;
            }
            playersRankList.get(i).setRank(currentRank);
        }
    }

    public void changeLeader(final Player player) {
        currentLeader = player;
    }

    public void skipTurn() {

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

    public Cell getCell(final int cellID) {
        return gameMap.getCell(cellID);
    }

    private static Comparator<Player> createRankComparator() {
        return new Comparator<Player>() {
            @Override
            public int compare(final Player p1, final Player p2) {
                if (p1.getStars() != p2.getStars()) {
                    return Integer.compare(p2.getStars(), p1.getStars());
                } else if (p1.getProperties().size() != p2.getProperties().size()) {
                    return Integer.compare(p2.getProperties().size(), p1.getProperties().size());
                } else {
                    return Integer.compare(p2.getCoins(), p1.getCoins());
                }
            }
        };
    }


}

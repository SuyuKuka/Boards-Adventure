package controller;

import java.util.ArrayList;
import java.util.HashMap;

import view.game.GamePanel;
import view.game.boardpanel.entitygraphics.PlayerGraphics;
import view.game.boardpanel.entitygraphics.cellsgraphics.CellGraphics;
import game.Game;
import game.GameState;
import model.Model;
import model.Player;
import model.config.cells.Cell;
import model.config.cells.CellType;
import model.config.cells.Coins;
import model.config.cells.None;
import model.config.cells.Property;
import model.config.cells.Start;

/**
 * Classe qui fait office de Contrôleur pour le {@link Model} et la {@link View}.
 * <p> Elle contient un {@link Model} qui s'occupe de la logique du jeu et d'un
 * {@link GamePanel} qui s'occupe de la vue du jeu et fait le lien entre ces 2 classes.
 */
public final class Controller {
    // Game
    private final Game game;
    private GameState prevGameState;
    // Model
    private final Model model;
    // View
    private GamePanel gamePanel;
    private HashMap<Integer, CellGraphics> cellsGraphics;
    private ArrayList<PlayerGraphics> playersGraphics;

    // Jeu
    private Cell currentCell;

    public Controller(final Game game, final Model model) {
        this.game = game;
        this.prevGameState = null;
        this.model = model;
    }

    public void gameUpdate() {
        if (prevGameState == null) {
            gameStart();
            return;
        }
        switch (prevGameState) {
            case GAME_START:
                newTurn();
                break;
            case NEW_TURN:
                if (getCurrentPlayer().isJailed()) {
                    inJail();
                } else {
                    playerAction();
                }
                break;
            case IN_JAIL:
                playerAction();
                break;
            case ROLL_DICE:
                playerMove();
                break;
            case PLAYER_MOVE:
                final Cell cell = getCurrentPlayerCell();
                if (cell.shouldPlayerStopBy() || getCurrentPlayer().getDiceRollValue() <= 0 || cell.isStarCell()) {
                    playerArrived();
                } else {
                    playerMove();
                }
                break;
            case PLAYER_ARRIVED:
                if (getCurrentPlayer().getDiceRollValue() > 0) {
                    playerMove();
                } else {
                    endTurn();
                }
                break;
            case END_TURN:
                if (model.getCurrentTurn() == model.getTotalTurns()) {
                    gameEnd();
                } else {
                    newTurn();
                }
                break;
            default:
                break;
        }
    }

    // region gameState

    private void gameStart() {
        prevGameState = GameState.GAME_START;
        gamePanel.gameStart();
    }

    private void newTurn() {
        prevGameState = GameState.NEW_TURN;
        model.newTurn();
        gamePanel.newTurn();
    }

    private void inJail() {
        prevGameState = GameState.IN_JAIL;
        gamePanel.inJail();
    }

    // region playerAction
    private void playerAction() {
        prevGameState = GameState.PLAYER_ACTION;
        final Player player = getCurrentPlayer();
        if (!player.isHuman()) {
            player.rollDice();
        }
        gamePanel.playerAction();
    }

    public void rollDice() {
        prevGameState = GameState.ROLL_DICE;
        model.rollDice();
        gamePanel.rollDice();
    }

    // endregion

    private void playerMove() {
        prevGameState = GameState.PLAYER_MOVE;
        final int diceRollValue = getCurrentPlayerRollDiceValue();
        model.playerMove();
        gamePanel.playerMove(diceRollValue);
    }

    private void playerArrived() {
        prevGameState = GameState.PLAYER_ARRIVED;
        final boolean isStarCell = getCurrentPlayerCellIsStar();
        model.playerArrived();
        gamePanel.playerArrived(isStarCell);
        updatePlayersScore();
    }

    public void updatePlayersScore() {
        model.updatePlayersRank();
        gamePanel.updatePlayersScoreGraphics();
    }

    public ArrayList<Integer> getPlayersRankList() {
        final ArrayList<Integer> rankList = new ArrayList<>();
        final ArrayList<Player> playerList = model.getPlayers();
        for (int i = 0; i < playerList.size(); i++) {
            rankList.add(playerList.get(i).getRank());
        }
        return rankList;
    }

    private void endTurn() {
        prevGameState = GameState.END_TURN;
        model.endTurn();
        gamePanel.endTurn();
    }

    private void gameEnd() {
        prevGameState = GameState.GAME_END;
        gamePanel.gameEnd();
    }

    // endregion

    // region controller methods

    public void setPlayerPosition(final int cellID) {
        getCurrentPlayer().setPosition(cellID);
    }

    public void playerRollDice() {
        final Player currentPlayer = getCurrentPlayer();
        currentPlayer.rollDice();
        if (currentPlayer.isJailed() && currentPlayer.getDiceRollValue() == 6) {
            currentPlayer.setJailTurn(0);
        }
    }

    public void playerBuyProperty() {
        ((Property) currentCell).buy(getCurrentPlayer());
    }

    public void playerUpgradeProperty() {
        ((Property) currentCell).upgrade(getCurrentPlayer());
    }

    public boolean shouldDiceRollValueDecrement(final int cellID) {
        final Cell cell = model.getGameMap().getCell(cellID);
        return !(cell instanceof None || cell.getShouldPlayerStopByValue() == 0);
    }

    public void playShowCard() {
        //TODO stocker la carte dans Joueur et pas dans Card avant de continuer
        // ((Card) currentCell).showCard(getCurrentPlayer(), model);
    }

    // #endregion

    // region model getters/setters

    public CellType getCellType(final String cellType) {
        return CellType.valueOf(cellType);
    }

    public Cell getCell(final int cellID) {
        return model.getGameMap().getCell(cellID);
    }

    public Coins getCoins(final int cellID) {
        return (Coins) model.getGameMap().getCell(cellID);
    }

    public int getCurrentPlayerPosition() {
        return getCurrentPlayer().getPosition();
    }

    public Cell getCurrentPlayerCell() {
        return model.getGameMap().getCell(getCurrentPlayerPosition());
    }

    public int getCurrentPlayerCellCoinsValue() {
        return ((Coins) getCurrentPlayerCell()).getValue();
    }

    public CellType getCurrentPlayerCellType() {
        return model.getGameMap().getCell(getCurrentPlayerPosition()).getCellType();
    }

    public void setCurrentCell() {
        this.currentCell = model.getGameMap().getCell(getCurrentPlayerPosition());
    }

    public Cell getPlayerCell(final int playerID) {
        return getPlayer(playerID).getPlayerCell(model);
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public boolean canPlayerUseStartItem(final int playerID) {
        return getPlayer(playerID).canUseStartItem();
    }

    public String getCardEffect() {
        //TODO stocker la carte dans Joueur et pas dans Card avant de continuer
        //return ((Card) currentCell).getCardEffect().toString();
        return "carte !!";
    }

    public Start getStartCell() {
        return model.getGameMap().getStartCell();
    }

    public ArrayList<Player> getPlayers() {
        return model.getPlayers();
    }

    public Player getPlayer(final int playerId) {
        return model.getPlayers().get(playerId);
    }


    public int getTotalTurns() {
        return model.getTotalTurns();
    }

    public int getCurrentTurn() {
        return model.getCurrentTurn();
    }

    public int getStarPrice() {
        return model.getStarPrice();
    }

    public Player getCurrentPlayer() {
        return model.getCurrentPlayer();
    }

    public String getCurrentPlayerName() {
        return getCurrentPlayer().getName();
    }

    public int getCurrentPlayerID() {
        return model.getCurrentPlayer().getID();
    }

    public int getCurrentPlayerCoins() {
        return getCurrentPlayer().getCoins();
    }

    public int getCurrentPlayerStars() {
        return getCurrentPlayer().getStars();
    }

    public boolean getCurrentPlayerCellIsStar() {
        return getCurrentPlayerCell().isStarCell();
    }

    public boolean isStarCell(final int cellID) {
        return getCell(cellID).isStarCell();
    }

    public PlayerGraphics getCurrentPlayerGraphics() {
        return gamePanel.getPlayerGraphics(getCurrentPlayerID());
    }

    public int getPropertyPrice() {
        return ((Property) currentCell).getBuyPrices().get(((Property) currentCell).getPropertyState());
    }

    public int getRentPrice() { // diviser par 5 pour 20%, + ou - ?
        return ((Property) currentCell).getBuyPrices().get(((Property) currentCell).getPropertyState()) / 5;
    }

    public String getMapName() {
        return model.getGameMap().getMapName();
    }

    public boolean playerIsAtHome() {
        return ((Property) currentCell).isAtHome(getCurrentPlayer());
    }

    public boolean propertyHasOwner() {
        return ((Property) currentCell).hasOwner();
    }

    public void getCurrentPlayerRollDice() {
        getCurrentPlayer().rollDice();
    }

    public int getCurrentPlayerRollDiceValue() {
        return getCurrentPlayer().getDiceRollValue();
    }

    public static int getPlayerItemShopNb() {
        return Player.getItemShopNb();
    }

    public boolean isCurrentPlayerHuman() {
        return getCurrentPlayer().isHuman();
    }


    // endregion

    public void setGamePanel(final GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    // region getters/setters

    public HashMap<String, ArrayList<Integer>> getPropertiesInfo() {
        final HashMap<String, ArrayList<Integer>> propertiesInfo = new HashMap<>();
        for (Cell cell : model.getGameMap().getCells().values()) {
            if (cell instanceof Property) {
                final Property property = (Property) cell;
                final String hexColor = property.getGroupHexColor();
                if (!propertiesInfo.containsKey(property.getGroupHexColor())) {
                    propertiesInfo.put(hexColor, new ArrayList<>());
                }
                propertiesInfo.get(hexColor).add(property.getId());
            }
        }
        return propertiesInfo;
    }

    public void setCellsGraphics(HashMap<Integer, CellGraphics> cellsGraphics) {
        this.cellsGraphics = cellsGraphics;
    }

    public HashMap<Integer, CellGraphics> getCellsGraphics() {
        return cellsGraphics;
    }

    public CellGraphics getCellGraphics(final int cellID) {
        return cellsGraphics.get(cellID);
    }

    public void setPlayersGraphics(ArrayList<PlayerGraphics> playersGraphics) {
        this.playersGraphics = playersGraphics;
    }

    public ArrayList<PlayerGraphics> getPlayersGraphics() {
        return playersGraphics;
    }

    public PlayerGraphics getPlayerGraphics(final int index) {
        return playersGraphics.get(index);
    }

    public CellGraphics getCurrentPlayerCellGraphics() {
        return cellsGraphics.get(getCurrentPlayerCell().getId());
    }

    // endregion
}

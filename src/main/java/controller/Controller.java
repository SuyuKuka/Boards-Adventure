package controller;

import java.util.ArrayList;

import view.game.GamePanel;
import game.Game;
import game.GameState;
import model.Model;
import model.config.cells.Cell;
import model.config.cells.CellType;
import model.config.cells.Gain;
import model.config.cells.Loss;
import model.config.cells.Property;
import model.config.cells.Start;
import model.config.players.Player;

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
                if (getCurrentPlayer().getDiceRollValue() > 0) {
                    playerMove();
                } else {
                    playerArrived();
                }
                break;
            case PLAYER_ARRIVED:
                endTurn();
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
        final int startCellID = getCurrentPlayerCell().getId();
        model.playerMove();
        gamePanel.playerMove(startCellID);
    }

    private void playerArrived() {
        prevGameState = GameState.PLAYER_ARRIVED;
        model.playerArrived();
        gamePanel.playerArrived();
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

    public void playerGainCoins() {
        getCurrentPlayer().incrementCoins(getGainedMoney());
    }

    public void playerLossMoney() {
        getCurrentPlayer().incrementCoins(-getLossMoney());
    }

    public void playerBuyProperty() {
        ((Property) currentCell).buy(getCurrentPlayer());
    }

    public void playerUpgradeProperty() {
        ((Property) currentCell).upgrade(getCurrentPlayer());
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

    public int getCurrentPlayerPosition() {
        return getCurrentPlayer().getPosition();
    }

    public Cell getCurrentPlayerCell() {
        return model.getGameMap().getCell(getCurrentPlayerPosition());
    }

    public CellType getCurrentPlayerCellType() {
        return model.getGameMap().getCell(getCurrentPlayerPosition()).getCellType();
    }

    public void setCurrentCell() {
        this.currentCell = model.getGameMap().getCell(getCurrentPlayerPosition());
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

    public int getGainedMoney() {
        return ((Gain) currentCell).getGain();
    }

    public int getLossMoney() {
        return ((Loss) currentCell).getLoss();
    }

    public int getGamePoint() {
        return getCurrentPlayer().getGamePoint();
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

    public boolean getRollDiceState() {
        if (prevGameState == GameState.PLAYER_ACTION) {
            return true;
        }
        System.out.println(prevGameState);
        return false;
    }

    public static int getPlayerItemShopNb() {
        return Player.getItemShopNb();
    }


    // endregion

    public void setGamePanel(final GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    // region getters/setters

    // endregion
}

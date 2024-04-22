package controller;

import java.util.ArrayList;

import javax.swing.JComponent;

import controller.resources.Resources;
import view.View;
import view.game.GamePanel;

import game.Game;
import game.GameState;
import model.Model;
import model.config.cells.Cell;
import model.config.cells.CellType;
import model.config.cells.Gain;
import model.config.cells.Loss;
import model.config.cells.Property;
import model.config.players.Player;
import model.config.cells.Card;

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
    private final GamePanel gamePanel;

    // Jeu
    private Cell currentCell;

    public Controller(final Game game, final Model model, final View view) {
        this.game = game;
        this.prevGameState = null;
        this.model = model;
        Resources.loadGameResources(model.getGameMap().getMapName());
        gamePanel = view.initNewGamePanel(this);
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
                if (getCurrentPlayer().getDiceRollValue() > 0 || getCurrentPlayerCell().shouldPlayerStop()) {
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
        gameUpdate();
    }

    // endregion

    private void playerMove() {
        prevGameState = GameState.PLAYER_MOVE;
        model.playerMove();
        gamePanel.playerMove();
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

    public void playerGainMoney() {
        getCurrentPlayer().getTeam().incrementMoney(getGainedMoney());
    }

    public void playerLossMoney() {
        getCurrentPlayer().getTeam().incrementMoney(-getLossMoney());
    }

    public void playerBuyProperty() {
        ((Property) currentCell).buy(getCurrentPlayer());
    }

    public void playerUpgradeProperty() {
        ((Property) currentCell).upgrade(getCurrentPlayer());
    }

    public void playShowCard() {
        ((Card) currentCell).showCard(getCurrentPlayer(), model);
    }

    // #endregion

    // region model getters/setters

    public Cell getCell(final int cellID) {
        return model.getGameMap().getCell(cellID);
    }

    public Cell getCurrentPlayerCell() {
        return model.getGameMap().getCell(getCurrentPlayer().getPosition());
    }

    public CellType getCurrentPlayerCellType() {
        return model.getGameMap().getCell(getCurrentPlayer().getPosition()).getCellType();
    }

    public void setCurrentCell() {
        this.currentCell = model.getGameMap().getCell(getCurrentPlayer().getPosition());
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public String getCardEffect() {
        return ((Card) currentCell).getCardEffect().toString();
    }

    public ArrayList<Player> getPlayers() {
        return model.getPlayers();
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

    public int getCurrentMoney() {
        return getCurrentPlayer().getTeam().getMoney();
    }

    public int getGainedMoney() {
        return ((Gain) currentCell).getGain();
    }

    public int getLossMoney() {
        return ((Loss) currentCell).getLoss();
    }

    public int getGamePoint() {
        return getCurrentPlayer().getTeam().getGamePoint();
    }

    public int getPropertyPrice() {
        return ((Property) currentCell).getBuyPrices().get(((Property) currentCell).getPropertyState());
    }

    public int getRentPrice() { // diviser par 5 pour 20%, + ou - ?
        return ((Property) currentCell).getBuyPrices().get(((Property) currentCell).getPropertyState()) / 5;
    }

    public boolean playerIsAtHome() {
        return ((Property) currentCell).isAtHome(getCurrentPlayer());
    }

    public boolean propertyHasOwner() {
        return ((Property) currentCell).hasOwner();
    }

    public void delete(final JComponent component) {
        gamePanel.remove(component);
    }

    // endregion

    // region getters/setters

    public String getMapName() {
        return model.getGameMap().getMapName();
    }

    // endregion
}

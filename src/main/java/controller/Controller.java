package controller;

import java.util.ArrayList;

import javax.swing.JComponent;

import controller.resources.Resources;
import view.View;
import view.game.GamePanel;

import game.Game;
import model.Model;
import model.config.cells.Cell;
import model.config.cells.Gain;
import model.config.cells.Loss;
import model.config.cells.Property;
import model.config.players.Player;

/**
 * Classe qui fait office de Contrôleur pour le {@link Model} et la {@link View}.
 * <p> Elle contient un {@link Model} qui s'occupe de la logique du jeu et d'un
 * {@link GamePanel} qui s'occupe de la vue du jeu et fait le lien entre ces 2 classes.
 */
public final class Controller {
    // Game
    private final Game game;
    // Model
    private final Model model;
    // View
    private final GamePanel gamePanel;

    // Jeu
    private Cell currentCell;

    public Controller(final Game game, final Model model, final View view) {
        this.game = game;
        this.model = model;
        Resources.loadGameResources(model.getGameMap().getMapName());
        gamePanel = view.initNewGamePanel(this);
        gamePanel.revalidate();
    }

    // region controller methods

    public void playerRollDice(final String diceName) {
        final Player currentPlayer = getCurrentPlayer();
        currentPlayer.rollDice(diceName);
        if (currentPlayer.isJailed() && currentPlayer.getDiceRollValue() == 6) {
            currentPlayer.setJailTurn(0);
        }
    }

    public void playerChooseIntersection(final int cellID) {
        getCurrentPlayer().setIntersectionCellID(cellID);
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

    // region model getters/setters

    public Cell getCell(final int cellID) {
        return model.getGameMap().getCell(cellID);
    }

    public Cell getCurrentPlayerCell() {
        return model.getGameMap().getCell(getCurrentPlayer().getPosition());
    }

    public void setCurrentCell() {
        this.currentCell = model.getGameMap().getCell(getCurrentPlayer().getPosition());
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

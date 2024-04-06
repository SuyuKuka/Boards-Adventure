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
 * Classe qui fait office de Controlleur pour le {@link Model} qui appelle {@link #run()}. <p>
 * Elle contient un {@link Model} qui s'occupe de la logique du jeu et d'un
 * {@link GamePanel} qui s'occupe de la vue du jeu et fait le lien entre ces 2 classes.
 */
public final class Controller {
    private final Game game;

    private final Model model;
    private final GamePanel gamePanel;
    private Cell currentCell;
    /** Vrai si le model devrait se mettre à jour. */
    private boolean shouldStateUpdate;

    public Controller(final Game game, final Model model, final View view) {
        this.game = game;
        this.model = model;
        gamePanel = view.initNewGamePanel(this, Resources.getGameMapGraphics());
        shouldStateUpdate = true;
        gamePanel.revalidate();
        run();
    }

    // region controller methods

    /**
     * Méthode mère qui fait fonctionner le jeu en mettant à jour constament la vue
     * et en mettant à jour le model seulement si necéssaire.
     */
    public void run() {
        if (shouldStateUpdate) {
            System.out.println(model.getGameState());
            model.update();
            gamePanel.render(model.getGameState());
            shouldStateUpdate = false;
        }
        gamePanel.animate(model.getGameState());
    }

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

    public Cell getCurrentPlayerCell() {
        return model.getGameMap().getCell(getCurrentPlayer().getPosition());
    }

    public void setCurrentCell() {
        this.currentCell = model.getGameMap().getCell(getCurrentPlayer().getPosition());
    }

    public void setShouldStateUpdate(final boolean shouldStateUpdate) {
        this.shouldStateUpdate = shouldStateUpdate;
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

    // endregion
}

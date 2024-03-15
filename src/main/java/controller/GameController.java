package controller;

import java.util.ArrayList;

import javax.swing.JComponent;

import view.View;
import view.game.GameMapGraphics;
import view.game.GamePanel;

import game.Game;
import model.Model;
import model.config.cells.Cell;
import model.config.players.Player;

/**
 * Classe qui fait office de Controlleur pour le jeu qui appelle {@link #run()}. <p>
 * Elle contient un {@link Model} qui s'occupe de la logique du jeu et d'un
 * {@link GamePanel} qui s'occupe de la vue du jeu et fait le lien entre ces 2 classes.
 */
public final class GameController extends Controller {

    private final Model model;
    private final GamePanel gamePanel;
    /** Vrai si le model devrait se mettre à jour. */
    private boolean shouldGameUpdate;

    public GameController(final Game game, final View view, final GameMapGraphics gameMapGraphics, final Model model) {
        super(game);
        this.model = model;
        gamePanel = view.initNewGamePanel(this, gameMapGraphics);
        initValues();
    }

    private void initValues() {
        shouldGameUpdate = false;
    }

    // region controller methods

    /**
     * Méthode mère qui fait fonctionner le jeu en mettant à jour constament la vue
     * et en mettant à jour le model seulement si necéssaire.
     */
    public void run() {
        while (true) {
            if (shouldGameUpdate) {
                model.update();
                gamePanel.resetState();
                shouldGameUpdate = false;
            }
            gamePanel.render(model.getGameState());
            updateNanoUpTime(System.nanoTime());
        }
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

    public Cell getCurrentPlayerCell() {
        return model.getGameMap().getCell(getCurrentPlayer().getPosition());
    }

    // region model getters/setters

    public void setShouldStateUpdate(final boolean shouldStateUpdate) {
        this.shouldGameUpdate = shouldStateUpdate;
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

    public void delete(final JComponent component) {
        gamePanel.remove(component);
    }

    // endregion

    // region getters/setters

    // endregion
}

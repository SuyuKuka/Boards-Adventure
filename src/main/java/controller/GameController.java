package controller;

import java.util.ArrayList;

import view.GameMapGraphics;
import view.View;

import view.game.GamePanel;

import game.Game;
import model.Model;
import model.config.cells.Cell;
import model.config.players.Player;

public final class GameController extends Controller {

    private final Model model;
    private final GamePanel gamePanel;

    private boolean shouldGameUpdate;

    public GameController(final Game game, final View view, final GameMapGraphics gameMapGraphics) {
        super(game);
        this.model = game.createTestModel(gameMapGraphics.getGameMap());
        gamePanel = view.initNewGamePanel(this, gameMapGraphics);
        initValues();
    }

    private void initValues() {
        shouldGameUpdate = false;
    }

    // region controller methods

    public void run() {
        while (true) {
            if (shouldGameUpdate) {
                model.update();
                shouldGameUpdate = false;
            }
            gamePanel.render(model.getGameState());
            updateNanoUpTime(System.nanoTime());
        }
    }

    public Cell getCurrentPlayerCell() {
        return model.getGameMap().getCell(getCurrentPlayer().getPosition());
    }

    // region model getters/setters

    public void setShouldGameUpdate(final boolean shouldGameUpdate) {
        this.shouldGameUpdate = shouldGameUpdate;
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

    // endregion

    // region getters/setters

    // endregion
}

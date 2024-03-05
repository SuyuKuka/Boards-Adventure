package controller;

import java.util.ArrayList;

import view.CorePanel;
import view.game.GamePanel;

import game.Game;
import model.Model;
import model.config.cells.CellType;
import model.config.players.Player;

public final class GameController extends Controller {

    private final Model model;
    private GamePanel gamePanel;

    private GameState gameState;

    public GameController(final Game game, final Model model) {
        super(game);
        this.model = model;
        gameState = GameState.GAME_START;
    }

    // region controller methods

    public void run() {
        while (true) {
            final double lastUpdate = (System.nanoTime() - getNanoUpTime() - getNanoStartTime()) / getNanoS();
            updateNanoUpTime(System.nanoTime());
            gamePanel.render(lastUpdate);
        }
    }

    public void updateGameState() {
        Player player = getCurrentPlayer();
        switch (gameState) {
            case GAME_START:
                gameState = GameState.NEW_TURN;
                break;
            case NEW_TURN:
                if (player.isJailed()) {
                    gameState = GameState.IN_JAIL;
                } else {
                    gameState = GameState.PLAY_DICE;
                }
                break;
            case PLAY_DICE:
                CellType cellType = model.getGameMap().getCell(player.getPosition()).getCellType();
                if (cellType == CellType.INTERSECTION) {
                    gameState = GameState.ASK_INTERSECTION;
                } else if (cellType == CellType.PROPERTY) {
                    gameState = GameState.ASK_PROPERTY;
                } else if (cellType == CellType.SHOP) {
                    gameState = GameState.ASK_SHOP;
                } else {
                    gameState = GameState.END_TURN;
                }
                break;
            default:
                break;
        }
    }

    public void playDice() {
        model.playDice();
        gamePanel.playDice();
    }

    // region model getters/setters

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

    public void setGamePanel(final GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(final GameState gameState) {
        this.gameState = gameState;
    }

    public void setCorePanel(final CorePanel corePanel) {
        gamePanel = (GamePanel) corePanel;
    }

    // endregion
}

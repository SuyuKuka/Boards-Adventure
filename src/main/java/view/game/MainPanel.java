package view.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import org.json.JSONObject;

import view.game.actionpanel.ActionPanel;
import view.game.boardpanel.BoardPanel;

public final class MainPanel extends JPanel {

    private final GamePanel gamePanel;

    private final BoardPanel boardPanel;
    private final ActionPanel actionPanel;

    public MainPanel(final GamePanel gamePanel, final JSONObject mapConfig) {
        this.gamePanel = gamePanel;
        final Dimension dim = gamePanel.getPreferredSize();
        setSize(dim.width, dim.height);
        setLayout(new BorderLayout());

        final int height1 = (int) (dim.height * 0.8);
        final int height2 = dim.height - height1;

        boardPanel = new BoardPanel(gamePanel, mapConfig, dim.width, height1);
        actionPanel = new ActionPanel(gamePanel, dim.width, height2);

        add(boardPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Dimension dim = gamePanel.getPreferredSize();
                final int height1 = (int) (dim.height * 0.8);
                final int height2 = dim.height - height1;
                boardPanel.setPreferredSize(new Dimension(dim.width, height1));
                actionPanel.setPreferredSize(new Dimension(dim.width, height2));
                revalidate();
                repaint();
            }
        });

        setVisible(true);
    }

    //#region update

    //#endregion

    //#region gameState

    public void gameStart() {
        boardPanel.gameStart();
        actionPanel.gameStart();
    }

    public void newTurn() {
        boardPanel.newTurn();
        actionPanel.newTurn();
    }

    public void inJail() {
        boardPanel.inJail();
        actionPanel.inJail();
    }

    public void playerAction() {
        actionPanel.playerAction();
    }

    public void rollDice() {
        actionPanel.rollDice();
        boardPanel.rollDice();
    }

    public void playerMove(final int diceRollValue) {
        boardPanel.playerMove(diceRollValue);
    }

    public void playerArrived() {
        boardPanel.playerArrived();
        actionPanel.playerArrived();
    }


    public void endTurn() {
        boardPanel.endTurn();
        actionPanel.endTurn();
    }

    public void gameEnd() {
        boardPanel.gameEnd();
        actionPanel.gameEnd();
    }

    //#endregion gameState

    public GamePanel getGamePanel() {
        return gamePanel;
    }

}

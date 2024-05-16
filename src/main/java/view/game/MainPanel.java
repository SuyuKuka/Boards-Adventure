package view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import org.json.JSONObject;

import view.game.actionpanel.ActionPanel;
import view.game.boardpanel.BoardPanel;
import view.game.datapanel.DataPanel;

public final class MainPanel extends JPanel {

    private final GamePanel gamePanel;

    private final DataPanel dataPanel;
    private final BoardPanel boardPanel;
    private final ActionPanel actionPanel;

    public MainPanel(final GamePanel gamePanel, final JSONObject mapConfig) {
        this.gamePanel = gamePanel;
        final Dimension dim = gamePanel.getPreferredSize();
        setBounds(0, 0, dim.width, dim.height);
        setSize(dim.width, dim.height);
        setBackground(Color.PINK);
        setLayout(new BorderLayout());

        final int width1 = (int) (dim.width * 0.2);
        final int width2 = dim.width - width1;
        final int height1 = (int) (dim.height * 0.8);
        final int height2 = dim.height - height1;

        boardPanel = new BoardPanel(gamePanel, mapConfig, width2, height1);
        dataPanel = new DataPanel(gamePanel, width1, height1);
        actionPanel = new ActionPanel(gamePanel, dim.width, height2);

        add(dataPanel, BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);

        add(actionPanel, BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Dimension dim = gamePanel.getPreferredSize();
                final int width1 = (int) (dim.width * 0.2);
                final int width2 = dim.width - width1;
                final int height1 = (int) (dim.height * 0.8);
                final int height2 = dim.height - height1;
                boardPanel.setPreferredSize(new Dimension(width2, height1));
                dataPanel.setPreferredSize(new Dimension(width1, height1));
                actionPanel.setPreferredSize(new Dimension(dim.height, height2));
                revalidate();
                repaint();
            }
        });

        setVisible(true);
    }

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
    }

    public void playerMove() {
        boardPanel.playerMove();
    }

    public void playerArrived() {
        boardPanel.playerArrived();
        actionPanel.playerArrived();
    }


    public void endTurn() {
        boardPanel.endTurn();
        dataPanel.endTurn();
        actionPanel.endTurn();
    }

    public void gameEnd() {
        boardPanel.gameEnd();
        actionPanel.gameEnd();
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}

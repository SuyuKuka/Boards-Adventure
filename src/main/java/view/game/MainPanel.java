package view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import org.json.JSONArray;

import view.game.boardpanel.BoardPanel;
import view.game.boardpanel.PlayerGraphics;
import view.game.boardpanel.cellsgraphics.CellGraphics;
import view.game.datapanel.DataPanel;

public final class MainPanel extends JPanel {

    private final GamePanel gamePanel;

    private final DataPanel dataPanel;
    private final BoardPanel boardPanel;
    private final ChatPanel chatPanel;
    private final ActionPanel actionPanel;

    public MainPanel(final GamePanel gamePanel, final HashMap<Integer, CellGraphics> cellsGraphics,
            final JSONArray jCells, final ArrayList<PlayerGraphics> playersGraphics) {
        this.gamePanel = gamePanel;
        final Dimension dim = gamePanel.getPreferredSize();
        setBounds(0, 0, dim.width, dim.height);
        setSize(dim.width, dim.height);
        setBackground(Color.PINK);
        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();

        boardPanel = new BoardPanel(gamePanel,  cellsGraphics, jCells, playersGraphics,
            (int) (dim.width * 0.8), (int) (dim.height * 0.8));
        dataPanel = new DataPanel(gamePanel, (int) (dim.width * 0.2), (int) (dim.height * 0.8));
        chatPanel = new ChatPanel(gamePanel, (int) (dim.width * 0.2), (int) (dim.height * 0.2));
        actionPanel = new ActionPanel(gamePanel, (int) (dim.width * 0.8), (int) (dim.height * 0.2));

        add(dataPanel, gbc);
        add(boardPanel, gbc);

        gbc.gridy = 1;
        add(chatPanel, gbc);
        add(actionPanel, gbc);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Dimension dim = gamePanel.getPreferredSize();
                final int gridwidth1 = (int) (dim.width * 0.2);
                final int gridwidth2 = (int) (dim.width * 0.8);
                final int gridheight1 = (int) (dim.height * 0.8);
                final int gridheight2 = (int) (dim.height * 0.2);
                dataPanel.setPreferredSize(new Dimension(gridwidth1, gridheight1));
                boardPanel.setPreferredSize(new Dimension(gridwidth2, gridheight1));
                chatPanel.setPreferredSize(new Dimension(gridwidth1, gridheight2));
                actionPanel.setPreferredSize(new Dimension(gridwidth2, gridheight2));
                revalidate();
                repaint();
            }
        });

        setVisible(true);
    }

    public void gameStart() {
        boardPanel.gameStart();
    }

    public void newTurn() {
        boardPanel.newTurn();
    }

    public void inJail() {
        boardPanel.inJail();
    }

    public void playerAction() {
        actionPanel.playerAction();
    }

    public void playerMove() {
        boardPanel.playerMove();
    }

    public void playerArrived() {
        boardPanel.playerArrived();
    }


    public void endTurn() {
        boardPanel.endTurn();
        dataPanel.endTurn();
    }

    public void gameEnd() {
        // afficher un panel avec les points de chaque joueurs et bouton revenir au menu.
        return;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}

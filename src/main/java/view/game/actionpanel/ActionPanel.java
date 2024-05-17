package view.game.actionpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;

import controller.resources.Resources;
import view.game.GamePanel;

public final class ActionPanel extends JPanel {

    private final GamePanel gamePanel;

    private final ChatPanel chatPanel;

    private final JPanel inventoryPanel;
    private final JPanel buttonsPanel;
    private final JPanel minimapPanel;

    private JComboBox<Object> otherActionBox;
    private JButton rollDiceButton;

    public ActionPanel(final GamePanel gamePanel, final int width, final int height) {
        this.gamePanel = gamePanel;
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());
        setOpaque(false);

        final int width1 = (int) (width * 0.3);
        final int width2 = (int) (width * 0.3);
        final int width3 = (int) (width * 0.1);
        final int width4 = width - width1 - width2 - width3;

        this.chatPanel = new ChatPanel(gamePanel, width1, height);
        add(chatPanel, BorderLayout.WEST);

        inventoryPanel = createInventoryPanel(width2, height);
        add(inventoryPanel, BorderLayout.CENTER);

        buttonsPanel = createButtonsPanel(width3, height);
        minimapPanel = createMinimapPanel(width4, height);
        final JPanel buttonsMinimapPanel = createButtonsMinimapPanel();
        add(buttonsMinimapPanel, BorderLayout.EAST);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension dim = getSize();
                final int width1 = (int) (dim.width * 0.3);
                final int width2 = (int) (dim.width * 0.3);
                final int width3 = (int) (dim.width * 0.1);
                final int width4 = dim.width - width1 - width2 - width3;
                chatPanel.setPreferredSize(new Dimension(width1, dim.height));
                inventoryPanel.setPreferredSize(new Dimension(width2, dim.height));
                buttonsMinimapPanel.setPreferredSize(new Dimension(width3 + width4, dim.height));
                buttonsPanel.setPreferredSize(new Dimension(width3, dim.height));
                minimapPanel.setPreferredSize(new Dimension(width4, dim.height));
                revalidate();
                repaint();
            }
        });
        setVisible(true);
    }

    //#region gamestate

    public void gameStart() {
        chatPanel.gameStart();
    }

    public void newTurn() {
        chatPanel.newTurn();
    }

    public void inJail() {
        chatPanel.inJail();
    }

    public void playerAction() {
        rollDiceButton.setIcon(Resources.getGif("rolling_dice"));
        rollDiceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                gamePanel.getController().rollDice();
                rollDiceButton.removeMouseListener(this);
            }
        });
    }

    public void rollDice() {
        chatPanel.rollDice();
    }

    public void playerArrived() {
        chatPanel.playerArrived();
    }


    public void endTurn() {
        chatPanel.endTurn();
    }

    public void gameEnd() {
        chatPanel.gameEnd();
    }

    //#endregion


    // #region Inventory

    private JPanel createInventoryPanel(final int width, final int height) {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(Color.RED);
        return panel;
    }


    // #endregion

    // #region Buttons

    private JPanel createButtonsPanel(final int width, final int height) {
        final JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(Color.PINK);
        otherActionBox = new JComboBox<Object>(gamePanel.getItemsGraphics().values().toArray());
        rollDiceButton = new JButton();
        rollDiceButton.setIcon(new ImageIcon(Resources.getImage("dice1")));

        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        otherActionBox.setRenderer(listRenderer);
        otherActionBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    rollDiceButton.setEnabled(false);
                    Timer timer = new Timer(5000, t -> {
                        rollDiceButton.setEnabled(true);
                        ((Timer) t.getSource()).stop();
                    });
                    timer.start();
                }
            }
        });
        panel.add(otherActionBox);

        rollDiceButton.setBorderPainted(false);
        rollDiceButton.setOpaque(false);
        rollDiceButton.setContentAreaFilled(false);
        rollDiceButton.setFocusPainted(false);

        panel.add(rollDiceButton);
        return panel;
    }

    // #endregion

    // #region MiniMap

    private JPanel createMinimapPanel(final int width, final int height) {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(Color.PINK);
        return panel;
    }

    //#endregion

    private JPanel createButtonsMinimapPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(buttonsPanel, BorderLayout.WEST);
        panel.add(minimapPanel, BorderLayout.CENTER);
        panel.setVisible(true);
        return panel;
    }
}

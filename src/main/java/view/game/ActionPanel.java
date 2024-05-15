package view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridBagLayout;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;

public final class ActionPanel extends JPanel {


    private final GamePanel gamePanel;

    private final JPanel inventoryPanel;
    private final JPanel buttonsPanel;
    private final JPanel minimapPanel;

    private JComboBox<Object> otherActionBox;
    private JButton rollDiceButton;

    public ActionPanel(final GamePanel gamePanel, final int width, final int height) {
        this.gamePanel = gamePanel;
        setPreferredSize(new Dimension(width, height));
        setLayout(new GridBagLayout());
        setOpaque(false);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.weightx = 0.4;
        inventoryPanel = createInventoryPanel((int) (gbc.weightx * width), height);
        add(inventoryPanel, gbc);

        gbc.weightx = 0.2;
        buttonsPanel = createButtonsPanel((int) (gbc.weightx * width), height);
        add(buttonsPanel, gbc);

        gbc.weightx = 0.4;
        minimapPanel = createMinimapPanel((int) (gbc.weightx * width), height);
        add(minimapPanel, gbc);
    }

    public void playerAction() {
        rollDiceButton.setIcon(new ImageIcon("src/main/resources/images/game/rolling_dice.gif"));
        rollDiceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                gamePanel.getController().rollDice();
                rollDiceButton.removeMouseListener(this);
            }
        });
    }

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
        Object[] items =
            {
                // new ImageIcon("src/main/resources/maps/map4/j1.png"),
                new ImageIcon("src/main/resources/maps/map4/j0.png"),
                new ImageIcon("src/main/resources/maps/map4/j1.png"),
                new ImageIcon("src/main/resources/maps/map4/j2.png"),
                new ImageIcon("src/main/resources/maps/map4/j3.png")

            };
        otherActionBox = new JComboBox<Object>(items);
        rollDiceButton = new JButton();
        rollDiceButton.setIcon(new ImageIcon("src/main/resources/images/game/dice1.png"));

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
}

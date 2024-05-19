package view.game.actionpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import controller.resources.Resources;
import model.config.cells.CellType;
import model.config.cells.Property;
import view.game.GamePanel;

import java.util.ArrayList;
import java.util.HashMap;

public final class ActionPanel extends JPanel {

    private static final Image PROPERTY_IMAGE = Resources.getImage("property");

    private final GamePanel gamePanel;

    private final ChatPanel chatPanel;

    private final JPanel infoPanel;
    private final JPanel buttonsPanel;
    private final JPanel inventoryPanel;

    private JComboBox<Object> otherActionBox;
    private JButton rollDiceButton;

    public ActionPanel(final GamePanel gamePanel, final int width, final int height) {
        this.gamePanel = gamePanel;
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());
        setOpaque(false);

        final int nbPropertiesGroup = gamePanel.getPropertiesInfo().size();

        final int width1 = (int) (width * 0.25);
        final int width2 = (int) (width * 0.3);
        int width4 = (int) (width * 0.3);
        width4 -= width4 % nbPropertiesGroup;
        final int width3 = width - width1 - width2 - width4;

        this.chatPanel = new ChatPanel(gamePanel, width1, height);
        add(chatPanel, BorderLayout.WEST);

        infoPanel = createInfoPanel(width2, height);
        add(infoPanel, BorderLayout.CENTER);

        buttonsPanel = createButtonsPanel(width3, height);
        inventoryPanel = createInventoryPanel(width4, height);
        final JPanel buttonsInventoryPanel = createButtonsInventoryPanel();
        add(buttonsInventoryPanel, BorderLayout.EAST);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension dim = getSize();
                final int width1 = (int) (dim.width * 0.25);
                final int width2 = (int) (dim.width * 0.3);
                int width4 = (int) (dim.width * 0.3);
                width4 -= width4 % nbPropertiesGroup;
                final int width3 = dim.width - width1 - width2 - width4;
                chatPanel.setPreferredSize(new Dimension(width1, dim.height));
                infoPanel.setPreferredSize(new Dimension(width2, dim.height));
                buttonsInventoryPanel.setPreferredSize(new Dimension(width3 + width4, dim.height));
                buttonsPanel.setPreferredSize(new Dimension(width3, dim.height));
                inventoryPanel.setPreferredSize(new Dimension(width4, dim.height));
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

    public void playerArrived(final CellType cellType) {
        chatPanel.playerArrived(cellType);
    }


    public void endTurn() {
        chatPanel.endTurn();
    }

    public void gameEnd() {
        chatPanel.gameEnd();
    }

    //#endregion

    // #region Info

    private JPanel createInfoPanel(final int width, final int height) {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(Color.RED);
        return panel;
    }

    // #endregion

    private JPanel createButtonsInventoryPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(buttonsPanel, BorderLayout.WEST);
        panel.add(inventoryPanel, BorderLayout.CENTER);
        panel.setVisible(true);
        return panel;
    }

    // #region Buttons

    private JPanel createButtonsPanel(final int width, final int height) {
        final JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(Color.PINK);
        final ArrayList<Object> objects = new ArrayList<>();
        objects.add(new ImageIcon(Resources.getImage("farmerhoe")));
        otherActionBox = new JComboBox<Object>(objects.toArray());
        rollDiceButton = new JButton();
        rollDiceButton.setIcon(Resources.getGif("rolling_dice"));

        ((JLabel) otherActionBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);

        otherActionBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println(e.getItem());
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

    // #region Inventaire

    private JPanel createInventoryPanel(final int width, final int height) {
        final HashMap<String, ArrayList<Integer>> propertiesInfo = gamePanel.getPropertiesInfo();
        final int groupPropertiesNb = propertiesInfo.size();
        int maxProperties = 0;
        for (String key : propertiesInfo.keySet()) {
            final int size = propertiesInfo.get(key).size();
            if (size > maxProperties) {
                maxProperties = size;
            }
        }
        final JPanel containerPanel = new JPanel(new GridLayout(1, groupPropertiesNb));
        containerPanel.setPreferredSize(new Dimension(width, height));
        containerPanel.setOpaque(false);
        for (String hexColor : propertiesInfo.keySet()) {
            final JPanel groupPanel = new JPanel(new GridLayout(maxProperties, 1));
            initGroupPropertyPanel(groupPanel, hexColor, propertiesInfo.get(hexColor),
                width / groupPropertiesNb, height / maxProperties);
            containerPanel.add(groupPanel);
        }
        return containerPanel;
    }

    private void initGroupPropertyPanel(final JPanel panel, final String hexColor,
            final ArrayList<Integer> propertiesID, final int width, final int height) {
        for (int i = 0; i < propertiesID.size(); i++) {
            final int propertyCellID = propertiesID.get(i);
            final JButton propertyButton = new JButton() {
                @Override
                protected void paintComponent(final Graphics g) {
                    final Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    final Dimension dim = getSize();
                    g2d.setColor(Color.decode(hexColor));
                    g2d.fillRect(0, 0, dim.width, dim.height);
                    final Property property = ((Property) gamePanel.getController().getCell(propertyCellID));
                    if (property.getOwner() == null) {
                        g2d.setColor(Color.WHITE);
                    } else {
                        g2d.setColor(
                            Color.decode(property.getOwner().getHexTeamColor()));
                    }
                    // Couleur intérieur
                    final int widthInside = (int) (dim.width * 0.9);
                    final int heightInside = (int) (dim.height * 0.9);
                    final int xInside = (dim.width - widthInside) / 2;
                    final int yInside = (dim.height - heightInside) / 2;
                    g2d.fillRoundRect(xInside, yInside, widthInside, heightInside, 5, 5);
                    // Icône
                    final int widthIcon = (int) (dim.width * 0.7);
                    final int heightIcon = (int) (dim.height * 0.7);
                    final int xIcon = (dim.width - widthIcon) / 2;
                    final int yIcon = (dim.height - heightIcon) / 2;
                    g2d.drawImage(PROPERTY_IMAGE, xIcon, yIcon, widthIcon, heightIcon, this);
                }
            };
            propertyButton.setSize(new Dimension(width, height));
            panel.add(propertyButton);
        }
    }

    //#endregion

}

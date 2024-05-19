package view.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import controller.resources.Resources;
import view.SwingFactory;

public final class PlayerConfigPanel extends JPanel {

    private MenuPanel menuPanel;
    private int height;
    private int width;

    private int defaultFont;

    private HashMap<String, String> gameConfig;
    private ArrayList<HashMap<String, String>> playersConfig = new ArrayList<>();

    private JPanel error;
    private JPanel configCharacter;
    private JPanel addPlayer4;
    private JPanel addPlayer3;
    private JPanel addPlayer2;

    private int selectItem = 1;

    public PlayerConfigPanel(final MenuPanel menuPanel, final HashMap<String, String> gameConfig) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.menuPanel = menuPanel;
        height = menuPanel.getHeight();
        width = menuPanel.getWidth();
        this.gameConfig = gameConfig;
        setSize(new Dimension(width, height));
        this.defaultFont = menuPanel.getDefaultFont();

        add(SwingFactory.createTransparentPanel(this, 100, 10));
        initError();
        add(error);

        add(createConfigCharacter(this));
        add(SwingFactory.createTransparentPanel(this, 100, 50));
        add(createBottomPanel(this));
    }

    private void initError() {
        error = SwingFactory.createTransparentPanel(this, 100, 10);
        error.setBackground(Color.RED);
        error.setAlignmentX(Component.CENTER_ALIGNMENT);
        error.setOpaque(true);
    }

    // #region Create Background

    private JPanel createBackground(int playerID) {
        final JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                final Dimension dim = getSize();
                final String team = playersConfig.get(playerID).get("teamColor");
                final String status = playersConfig.get(playerID).get("status");
                if (team.equals("red") && status.equals("false")) {
                    g.drawImage(Resources.getImage("red"), 0, 0, dim.width, dim.height, this);
                } else if (team.equals("blue") && status.equals("false")) {
                    g.drawImage(Resources.getImage("blue"), 0, 0, dim.width, dim.height, this);
                } else if (team.equals("none") && status.equals("false")) {
                    g.drawImage(Resources.getImage("none"), 0, 0, dim.width, dim.height, this);
                } else if (team.equals("red") && status.equals("true")) {
                    g.drawImage(Resources.getImage("redReady"), 0, 0, dim.width, dim.height, this);
                } else if (team.equals("blue") && status.equals("true")) {
                    g.drawImage(Resources.getImage("blueReady"), 0, 0, dim.width, dim.height, this);
                } else if (team.equals("none") && status.equals("true")) {
                    g.drawImage(Resources.getImage("noneReady"), 0, 0, dim.width, dim.height, this);
                }
            }
        };
        panel.setOpaque(false);
        panel.setSize(getPreferredSize());
        panel.setVisible(true);
        return panel;
    }

    // #endregion

    // #region Create Config Character

    private JPanel createConfigCharacter(final JPanel parent) {
        configCharacter = SwingFactory.createTransparentPanel(parent, 100, 80);
        configCharacter.setLayout(new GridLayout(0, 4));
        configCharacter.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel character1 = createCharacter(parent, 0);
        configCharacter.add(character1);

        addPlayer2 = createAddPlayer(parent, 1, addPlayer3);
        addPlayer3 = createAddPlayer(parent, 2, addPlayer4);
        addPlayer4 = createAddPlayer(parent, 3, null);

        configCharacter.add(addPlayer2);
        return configCharacter;
    }

    // #endregion

    // #region Create Character

    private JPanel createCharacter(final JPanel parent, final int playerID) {
        JPanel panel = createBackground(playerID);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel character = character(parent, playerID, true);
        panel.add(character);
        return panel;
    }

    private JPanel character(JPanel parent, int playerID, boolean player) {
        initCharacterValue(playerID, player);
        JPanel panel = createBackground(playerID);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel team = null;
        if (gameConfig.get("mode").equals("2v2")) {
            team = createTeam(parent, playerID);
            panel.add(team);
        }

        JPanel pseudo = createPseudo(parent, playerID);
        pseudo.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        panel.add(pseudo);

        JPanel skin = createSelectCharacter(parent, playerID);
        panel.add(skin);

        JPanel item = createSelectItem(parent, playerID);
        panel.add(item);

        JPanel status = createStatus(parent, playerID, team, (JTextPane) pseudo.getComponent(0), skin, item);
        panel.add(status);

        return panel;
    }

    private void initCharacterValue(final int playerID, final boolean player) {
        HashMap<String, String> newPlayer = new HashMap<>();
        newPlayer.put("teamColor", "none");
        newPlayer.put("name", player ? "Player " + (playersConfig.size() + 1) : "Ordinateur " + (playersConfig.size() + 1));
        newPlayer.put("selectSkin", "1");
        newPlayer.put("className", "Board");
        newPlayer.put("itemName", "farmer_hoe");
        newPlayer.put("status", "false");
        newPlayer.put("type", player ? "human" : "computer");
        playersConfig.add(playerID, newPlayer);
    }

    // #endregion

    // #region Create Team
    private JPanel createTeam(final JPanel parent, int playerID) {
        final JPanel panel = SwingFactory.createImagePanel(parent, "bluered", 20, 50);
        panel.setLayout(new GridLayout(1, 2));

        JButton blue = SwingFactory.createTextButton(parent, "BLEU", 20, 20, 32, false);
        blue.setForeground(Color.WHITE);
        blue.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                playersConfig.get(playerID).replace("teamColor", "blue");
            }
        });
        panel.add(blue, 0);

        JButton red = SwingFactory.createTextButton(parent, "ROUGE", 20, 20, 32, false);
        red.setForeground(Color.WHITE);
        red.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                playersConfig.get(playerID).replace("teamColor", "red");
            }
        });
        panel.add(red, 1);
        return panel;
    }

    //#endregion

    // #region Create Pseudo

    private JPanel createPseudo(final JPanel parent, int playerID) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 20);
        panel.setLayout(new BorderLayout());
        JTextPane pseudo = new JTextPane();
        pseudo.setText(playersConfig.get(playersConfig.size() - 1).get("name"));
        pseudo.setForeground(Color.BLACK);
        pseudo.setFont(Resources.getFont("NoitaBlackletter", 32));
        pseudo.setPreferredSize(new Dimension(200, 50));

        ((AbstractDocument) pseudo.getDocument()).setDocumentFilter(new DocumentFilter() {
        private final int maxChars = 10;

        @Override
        public void insertString(FilterBypass fb, int offset, String string,
            AttributeSet attr) throws BadLocationException {
            if ((fb.getDocument().getLength() + string.length()) <= maxChars) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length,
            String text, AttributeSet attrs) throws BadLocationException {
            if ((fb.getDocument().getLength() + text.length() - length) <= maxChars) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
        });

        StyledDocument doc = pseudo.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        panel.add(pseudo, BorderLayout.CENTER, 0);

        if (playerID != 0) {
            JButton delete = SwingFactory.createImageButton(parent, "delete", 4, 4, false);
            delete.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    playersConfig.remove(playerID);
                    if (playerID == 3) {
                        configCharacter.remove(addPlayer4);
                        addPlayer4 = createAddPlayer(parent, 3, null);
                        configCharacter.add(addPlayer4);
                    } else if (playerID == 2) {
                        configCharacter.remove(addPlayer3);
                        configCharacter.remove(addPlayer4);
                        addPlayer3 = createAddPlayer(parent, 2, addPlayer4);
                        configCharacter.add(addPlayer3);
                    } else if (playerID == 1) {
                        configCharacter.remove(addPlayer2);
                        configCharacter.remove(addPlayer3);
                        configCharacter.remove(addPlayer4);
                        addPlayer3 = createAddPlayer(parent, 2, addPlayer4);
                        addPlayer2 = createAddPlayer(parent, 1, addPlayer3);
                        configCharacter.add(addPlayer2);
                    }
                    configCharacter.revalidate();
                    configCharacter.repaint();
                }
            });
            panel.add(delete, BorderLayout.EAST);
        }
        return panel;
    }

    // #endregion

    // #region Create Select Character

    private JPanel createSelectCharacter(final JPanel parent, int playerID) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 80);
        panel.setLayout(new BorderLayout());
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int widthPer = 20;
        int heightPer = 20;
        Image skinImg = Resources.getImage(playersConfig.get(playerID).get("className"));

        JButton left = SwingFactory.createImageButton(parent, "left", 5, 10, false);
        left.setBorder(BorderFactory.createEmptyBorder(70, 0, 0, 0));
        panel.add(left, BorderLayout.WEST, 0);
        JLabel character = SwingFactory.createImageImageLabel(
            this, skinImg, widthPer, heightPer, false);
        panel.add(character, BorderLayout.CENTER, 1);
        JButton right = SwingFactory.createImageButton(parent, "right", 5, 10, false);
        right.setBorder(BorderFactory.createEmptyBorder(70, 0, 0, 0));
        panel.add(right, BorderLayout.EAST, 2);

        selectSkin(parent, character, playerID, 0, widthPer, heightPer);
        skinListener(left, parent, character, playerID, -1, widthPer, heightPer);
        skinListener(right, parent, character, playerID, 1,  widthPer, heightPer);
        return panel;
    }

    private void skinListener(JButton button, JPanel skinSelection, JLabel skinLabel, int playerID,
        int i, int widthPer, int heightPer) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectSkin(skinSelection, skinLabel, playerID, i, widthPer, heightPer);
                revalidate();
                repaint();
            }
        });
        menuPanel.handCursor(button);
    }

    // #endregion

    // #region Create Select Item

    private JPanel createSelectItem(final JPanel parent, int playerID) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 20);
        panel.setLayout(new BorderLayout());
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int widthPer = 10;
        int heightPer = 10;
        Image itemImg = Resources.getImage(playersConfig.get(playerID).get("itemName"));

        JButton left = SwingFactory.createImageButton(parent, "left", 4, 4, false);
        panel.add(left, BorderLayout.WEST, 0);
        JLabel item = SwingFactory.createImageImageLabel(
            parent, itemImg, widthPer, heightPer, false);
        panel.add(item, BorderLayout.CENTER, 1);
        JButton right = SwingFactory.createImageButton(parent, "right", 4, 4, false);
        panel.add(right, BorderLayout.EAST, 2);

        selectItem(parent, item, playerID, 0, 10, 10);
        itemListener(left, parent, item, playerID, -1, widthPer, heightPer);
        itemListener(right, parent, item, playerID, 1, widthPer, heightPer);
        return panel;
    }

    private void itemListener(JButton button, JPanel itemSelection, JLabel itemLabel, int playerID,
        int i, int widthPer, int heightPer) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectItem(itemSelection, itemLabel, playerID, i, widthPer, heightPer);
                revalidate();
                repaint();
            }
        });
        menuPanel.handCursor(button);
    }

    // #endregion

    // #region Create Status

    private JPanel createStatus(final JPanel parent, int playerID, JPanel team, JTextPane pseudoPane,
        JPanel skin, JPanel item) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 20);
        panel.setLayout(new GridLayout(1, 2));

        JButton cancel = new JButton("Annuler");
        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                playersConfig.get(playerID).replace("teamColor", "none");
                playersConfig.get(playerID).replace("status", "false");
            }
        });
        panel.add(cancel);

        JButton confirm = new JButton("Confirmer");
        confirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String updatedPseudo = pseudoPane.getText();
                playersConfig.get(playerID).put("name", updatedPseudo);
                playersConfig.get(playerID).replace("status", "true");
            }
        });
        panel.add(confirm);
        return panel;
    }

    // #endregion

    // #region Create Add Player

    private JPanel createAddPlayer(final JPanel parent, final int playerID, final JPanel nextPlayerPanel) {
        JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 20);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel center = SwingFactory.createTransparentPanel(parent, 100, 17);
        panel.add(center);
        JButton addPlayer = SwingFactory.createImageButton(parent, "add", 10, 10, false);
        addPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(addPlayer);

        JButton addBot = SwingFactory.createImageButton(parent, "bot", 10, 10, false);
        addBot.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(addBot);
        addPlayer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (playersConfig.size() == 1) {
                    configCharacter.add(addPlayer3);
                    configCharacter.revalidate();
                    configCharacter.repaint();
                } else if (playersConfig.size() == 2) {
                    configCharacter.add(addPlayer4);
                    configCharacter.revalidate();
                    configCharacter.repaint();
                }
                panel.remove(addPlayer);
                panel.remove(addBot);
                panel.remove(center);

                panel.add(character(parent, playerID, true));
                if (nextPlayerPanel != null) {
                    nextPlayerPanel.setVisible(true);
                }
            }
        });

        addBot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (playersConfig.size() == 1) {
                    configCharacter.add(addPlayer3);
                    configCharacter.revalidate();
                    configCharacter.repaint();
                } else if (playersConfig.size() == 2) {
                    configCharacter.add(addPlayer4);
                    configCharacter.revalidate();
                    configCharacter.repaint();
                }
                panel.remove(addPlayer);
                panel.remove(addBot);
                panel.remove(center);

                panel.add(character(parent, playerID, false));
                if (nextPlayerPanel != null) {
                    nextPlayerPanel.setVisible(true);
                }
            }
        });
        return panel;
    }

    // #endregion

    // #region Create Bottom Panel

    private JPanel createBottomPanel(final JPanel parent) {
        final JPanel panel = SwingFactory.createTransparentPanel(parent, 100, 10);
        panel.setLayout(new BorderLayout());
        final JButton back = SwingFactory.createTextButton(parent, "Retourner au configuration", 10, 10, 64, false);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                menuPanel.removeComp(MenuPanel.getPanelIndex());
                menuPanel.addComp(new GameConfigPanel(menuPanel), MenuPanel.getPanelIndex());
                menuPanel.setBackgroundImage(new ImageIcon(Resources.getImage("sign")));
                revalidate();
                repaint();
            }
        });
        menuPanel.handCursor(back);
        panel.add(back, BorderLayout.WEST);

        JButton startGame = SwingFactory.createTextButton(parent, "Lancez l'aventure", 10, 10, 64, false);
        startGame.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
        startGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int redTeam = 0;
                int blueTeam = 0;
                for (int i = 0; i < playersConfig.size(); i++) {
                    if (playersConfig.size() != 4 && gameConfig.get("mode").equals("2v2")) {
                        error.removeAll();
                        error.add(SwingFactory.createTextLabel("Pas assez de joueur", Color.BLACK, defaultFont / 2));
                        error.revalidate();
                        return;
                    } else if (playersConfig.get(i).get("teamColor").equals("none")
                        && gameConfig.get("mode").equals("2v2")) {
                        error.removeAll();
                        error.add(SwingFactory.createTextLabel(playersConfig.get(i).get("name")
                            + " n'a pas d'équipe", Color.BLACK, defaultFont / 2));
                        error.revalidate();
                        return;
                    } else if (playersConfig.get(i).get("teamColor").equals("red") && gameConfig.get("mode").equals("2v2")) {
                        redTeam++;
                        if (redTeam > 2) {
                            error.removeAll();
                            error.add(SwingFactory.createTextLabel("Trop de joueurs dans l'équipe rouge",
                                Color.BLACK, defaultFont / 2));
                            error.revalidate();
                            return;
                        }
                    } else if (playersConfig.get(i).get("teamColor").equals("blue")
                        && gameConfig.get("mode").equals("2v2")) {
                        blueTeam++;
                        if (blueTeam > 2) {
                            error.removeAll();
                            error.add(SwingFactory.createTextLabel("Trop de joueurs dans l'équipe bleue",
                                Color.BLACK, defaultFont / 2));
                            error.revalidate();
                            return;
                        }
                    }
                    if (playersConfig.get(i).get("status").equals("false")) {
                        error.removeAll();
                        error.add(SwingFactory.createTextLabel(playersConfig.get(i).get("name")
                            + " n'a pas confirmé ses configurations", Color.BLACK, defaultFont / 2));
                        error.revalidate();
                        return;
                    }
                }
                revalidate();
                repaint();
                // GAME START
                menuPanel.getFrame().getGame().startGame(gameConfig, playersConfig);
            }
        });
        menuPanel.handCursor(startGame);
        panel.add(startGame, BorderLayout.EAST);
        return panel;
    }

    // #endregion

    // #region Selector

    private void selectSkin(JPanel parent, JLabel skin, int playerID, int i, int widthPer, int heightPer) {
        int selectSkin = Integer.parseInt(playersConfig.get(playerID).get("selectSkin")) + i;
        if (selectSkin >= 6) {
            selectSkin = 1;
        } else if (selectSkin <= 0) {
            selectSkin = 5;
        }
        playersConfig.get(playerID).replace("selectSkin", String.valueOf(selectSkin));

        if ((i == 0 && playerID == 0)) {
            playersConfig.get(playerID).replace("className", "Board");
            final Dimension size = SwingFactory.calcDimension(new ImageIcon(Resources.getImage(
                playersConfig.get(playerID).get("className"))).getImage(), parent, widthPer, heightPer, false);
            skin.setPreferredSize(new Dimension((int) size.getWidth() * widthPer / 100,
                (int) (size.getHeight() * heightPer / 100)));

            skin.setIcon(new ImageIcon(Resources.getImage(playersConfig.get(playerID).get("className")).getScaledInstance(
                (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
            skin.repaint();
            skin.revalidate();
            return;
        } else if (i == 0 && playerID != 0) {
            i = 1;
        }

        switch (selectSkin) {
            case 1 :
                for (int j = 0; j < playersConfig.size(); j++) {
                    if (playersConfig.get(j).get("className").equals("Board")) {
                        selectSkin(parent, skin, playerID, i, widthPer, heightPer);
                        return;
                    }
                }
                playersConfig.get(playerID).replace("className", "Board");
                break;
            case 2 :
                for (int j = 0; j < playersConfig.size(); j++) {
                    if (playersConfig.get(j).get("className").equals("Howard")) {
                        selectSkin(parent, skin, playerID, i, widthPer, heightPer);
                        return;
                    }
                }
                playersConfig.get(playerID).replace("className", "Howard");
                break;
            case 3 :
                for (int j = 0; j < playersConfig.size(); j++) {
                    if (playersConfig.get(j).get("className").equals("Bacchus")) {
                        selectSkin(parent, skin, playerID, i, widthPer, heightPer);
                        return;
                    }
                }
                playersConfig.get(playerID).replace("className", "Bacchus");
                break;
            case 4 :
                for (int j = 0; j < playersConfig.size(); j++) {
                    if (playersConfig.get(j).get("className").equals("Gloria")) {
                        selectSkin(parent, skin, playerID, i, widthPer, heightPer);
                        return;
                    }
                }
                playersConfig.get(playerID).replace("className", "Gloria");
                break;
            case 5 :
                for (int j = 0; j < playersConfig.size(); j++) {
                    if (playersConfig.get(j).get("className").equals("May")) {
                        selectSkin(parent, skin, playerID, i, widthPer, heightPer);
                        return;
                    }
                }
                playersConfig.get(playerID).replace("className", "May");
                break;
            default:
                break;
        }

        final Dimension size = SwingFactory.calcDimension(new ImageIcon(Resources.getImage(
            playersConfig.get(playerID).get("className"))).getImage(), parent, widthPer, heightPer, false);
        skin.setPreferredSize(new Dimension((int) size.getWidth() * widthPer / 100,
            (int) (size.getHeight() * heightPer / 100)));

        skin.setIcon(new ImageIcon(Resources.getImage(playersConfig.get(playerID).get("className")).getScaledInstance(
            (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));

        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Dimension size = SwingFactory.calcDimension(Resources.getImage(
                    playersConfig.get(playerID).get("className")), parent, widthPer, heightPer, false);
                skin.setPreferredSize(new Dimension((int) size.getWidth() * widthPer / 100,
                    (int) (size.getWidth() * heightPer / 100)));
                skin.setIcon(new ImageIcon(Resources.getImage(playersConfig.get(playerID).get("className"))
                    .getScaledInstance((int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
            }
        });
        skin.repaint();
        skin.revalidate();
    }

    private void selectItem(JPanel parent, JLabel item, int playerID, int i, int widthPer, int heightPer) {
        selectItem += i;
        if (selectItem >= 6) {
            selectItem = 1;
        } else if (selectItem <= 0) {
            selectItem = 5;
        }

        switch (selectItem) {
            case 1 :
                playersConfig.get(playerID).replace("itemName", "farmer_hoe");
                break;
            case 2 :
                playersConfig.get(playerID).replace("itemName", "little_dice");
                break;
            case 3 :
                playersConfig.get(playerID).replace("itemName", "lucky_potion");
                break;
            case 4 :
                playersConfig.get(playerID).replace("itemName", "sacred_book");
                break;
            case 5 :
                playersConfig.get(playerID).replace("itemName", "magic_wand");
                break;
            default:
                break;
        }

        final Dimension size = SwingFactory.calcDimension(new ImageIcon(Resources.getImage(
            playersConfig.get(playerID).get("itemName"))).getImage(), parent, widthPer, heightPer, false);
        item.setPreferredSize(new Dimension((int) size.getWidth() * widthPer / 100,
            (int) (size.getHeight() * heightPer / 100)));
        item.setIcon(new ImageIcon(Resources.getImage(playersConfig.get(playerID).get("itemName")).getScaledInstance(
            (int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));

        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Dimension size = SwingFactory.calcDimension(Resources.getImage(
                    playersConfig.get(playerID).get("itemName")), parent, widthPer, heightPer, false);
                item.setPreferredSize(new Dimension((int) size.getWidth() * widthPer / 100,
                    (int) (size.getWidth() * heightPer / 100)));
                item.setIcon(new ImageIcon(Resources.getImage(playersConfig.get(playerID).get("itemName"))
                    .getScaledInstance((int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)));
            }
        });
        item.repaint();
        item.revalidate();
    }

    // #endregion
}

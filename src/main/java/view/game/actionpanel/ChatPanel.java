package view.game.actionpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SizeRequirements;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.InlineView;

import controller.Controller;
import model.config.cells.CellType;
import view.SwingFactory;
import view.game.GamePanel;

public final class ChatPanel extends JPanel {

    private final GamePanel gamePanel;
    private final Controller controller;

    private static final SimpleAttributeSet SAT = new SimpleAttributeSet();
    // Logs du jeu
    private static final Color LOG_COLOR = new Color(85, 149, 74);
    private final JTextPane logTextPane;

    // Chat global entre les joueurs
    private final JTextPane globalChatTextPane;


    static {
        StyleConstants.setFontSize(SAT, 14);
    }

    public ChatPanel(final GamePanel gamePanel, final int width, final int height) {
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());

        logTextPane = createTextPane();
        globalChatTextPane = createTextPane();

        add(createTabPanel(logTextPane, globalChatTextPane), BorderLayout.CENTER);

        setVisible(true);
    }

    //#region

    public void gameStart() {
        addLogText("Début du jeu !\n", true);
    }

    public void newTurn() {
        addLogText("Début du tour " + controller.getCurrentTurn() + " de "
            + controller.getCurrentPlayerName() + ".\n", true);
    }

    public void inJail() {
        addLogText(controller.getCurrentPlayerName() + " est en prison. Il lui reste encore "
            + controller.getCurrentPlayer().getJailTurn() + " tours pour essayer de s'échapper !\n", true);
    }

    public void rollDice() {
        addLogText(controller.getCurrentPlayerName() + " à lancé ses dés et obtient un "
            + controller.getCurrentPlayerRollDiceValue() + " !\n", true);
    }

    public void playerArrived(final boolean isStarCell) {
        if (isStarCell) {
            addStarLogText();
        } else {
            addGameLogText();
        }
    }

    private void addStarLogText() {
        final String playerName = controller.getCurrentPlayerName();
        final boolean isComputer = !controller.getCurrentPlayer().isHuman();
        addLogText(playerName + " tombe sur une case Etoile !!!!!\n", true);
        SwingFactory.startSingleActionTimer(500, action -> {
            if (!controller.getCurrentPlayerCellIsStar() && isComputer) {
                addBuyStarLogText();
            } else if (isComputer) {
                addNotBuyStarLogText();
            } else if (controller.getCurrentPlayer().getCoins() < controller.getStarPrice()) {
                addNotEnoughCoinsForStarLogText();
            }
        });

    }

    private void addNotEnoughCoinsForStarLogText() {
        final String playerName = controller.getCurrentPlayerName();
        addLogText(playerName + " n'a pas assez d'argents pour acheter l'étoile :(\n", false);
    }

    public void addBuyStarLogText() {
        final String playerName = controller.getCurrentPlayerName();
        addLogText(playerName + " achète l'étoile et en possède "
                + controller.getCurrentPlayer().getStars() + " désormais.\n", false);
    }

    public void addNotBuyStarLogText() {
        final String playerName = controller.getCurrentPlayerName();
        addLogText(playerName + " n'achète pas cette étoile pour une quelconque raison :(\n", false);
    }

    private void addGameLogText() {
        final CellType cellType = controller.getCurrentPlayerCellType();
        final String cellString = String.valueOf(cellType);
        final String playerName = controller.getCurrentPlayerName();
        final boolean playerStop = controller.getCurrentPlayerRollDiceValue() > 0;
        final String playerAction;
        if (playerStop) {
            playerAction = " se halte sur une case ";
        } else {
            playerAction = " arrête son chemin sur une case ";
        }
        addLogText(playerName + playerAction
            + cellString.substring(0, 1) + cellString.substring(1).toLowerCase() + " !\n", true);
        SwingFactory.startSingleActionTimer(500, action -> {
            switch (cellType) {
                case COINS:
                    final int value = controller.getCurrentPlayerCellCoinsValue();
                    if (value >= 0) {
                        addLogText(playerName + " a gagné " + value + " pièces !\n", false);
                    } else {
                        addLogText(playerName + " a perdu " + -value + " pièces :(\n", false);
                    }
                    break;
                case TELEPORTATION:
                    addLogText(playerName + " s'est téléporté !\n", false);
                    break;
                case VOID:
                    addLogText("Rien ne se passe...\n", false);
                    break;
                case DANGER:
                    addLogText("Quelque chose de mystérieux s'est passé...\n", false);
                    break;
                default:
                    break;
            }
        });
    }


    public void endTurn() {
        int turn = controller.getCurrentTurn();
        addLogText("Fin du tour " + turn + " de "
            + controller.getCurrentPlayerName() + ".\n", true);
    }

    public void gameEnd() {
        addLogText("Fin du jeu, merci d'avoir joué !\n", true);
    }

    //#endregion

    // #region Chat

    private JTabbedPane createTabPanel(final JTextPane logPane, final JTextPane globalPane) {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Systeme", createLogPanel(logPane));
        tabbedPane.add("Global", createChatPanel(globalPane));
        return tabbedPane;
    }

    private JPanel createLogPanel(final JTextPane textPane) {
        final JPanel panel = new JPanel(new BorderLayout());
        final JScrollPane scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createChatPanel(final JTextPane textPane) {
        final JPanel panel = new JPanel(new BorderLayout());
        final JScrollPane scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(createTextField(textPane), BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Crée une région de texte.
     * @return {@link JTextPane}
     */
    private JTextPane createTextPane() {
        final JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        // @see https://stackoverflow.com/a/7036671 permet d'avoir du letter wrap dans le textPane
        textPane.setEditorKit(new HTMLEditorKit() {
            @Override
            public ViewFactory getViewFactory() {
                return new HTMLFactory() {
                    public javax.swing.text.View create(final Element e) {
                        javax.swing.text.View v = super.create(e);
                        if (v instanceof InlineView) {
                            return new InlineView(e) {
                                public int getBreakWeight(int axis, float pos, float len) {
                                    return GoodBreakWeight;
                                }
                                public javax.swing.text.View breakView(int axis, int p0, float pos, float len) {
                                    if (axis == javax.swing.text.View.X_AXIS) {
                                        checkPainter();
                                        int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len);
                                        if (p0 == getStartOffset() && p1 == getEndOffset()) {
                                            return this;
                                        }
                                        return createFragment(p0, p1);
                                    }
                                    return this;
                                }
                            };
                        } else if (v instanceof ParagraphView) {
                            return new ParagraphView(e) {
                                protected SizeRequirements calculateMinorAxisRequirements(int axis,
                                    SizeRequirements r) {
                                    if (r == null) {
                                        r = new SizeRequirements();
                                    }
                                    float pref = layoutPool.getPreferredSpan(axis);
                                    float min = layoutPool.getMinimumSpan(axis);
                                    r.minimum = (int) min;
                                    r.preferred = Math.max(r.minimum, (int) pref);
                                    r.maximum = Integer.MAX_VALUE;
                                    r.alignment = 0.5f;
                                  return r;
                                }
                            };
                        }
                      return v;
                    }
                };
            }
        });
        return textPane;
    }

    /**
     * Méthode qui crée un {@link JTextField}, une zone de texte où l'utilisateur peut écrire des messages.
     * En appuyant sur {@link KeyEvent#VK_ENTER}, le message est passé sur le {@link JTextPane}.
     * @param textPane
     * @return {@link JTextField}
     */
    private JTextField createTextField(final JTextPane textPane) {
        final JTextField textField = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !textField.getText().equals("")) {
                    addDiscussionText(textPane, controller.getCurrentPlayerID(), textField.getText());
                    textField.setText("");
                }
            }
        });
        return textField;
    }

    private void addDiscussionText(final JTextPane textPane, final int playerID, final String text) {
        final Document doc = textPane.getDocument();
        try {
            StyleConstants.setForeground(SAT, Color.decode(controller.getPlayer(playerID).getHexTeamColor()));
            doc.insertString(doc.getLength(), "["
                + controller.getPlayer(playerID).getName() + "] : " + text + "\n", SAT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLogText(final String text, final boolean sayLogType) {
        final Document doc = logTextPane.getDocument();
        try {
            StyleConstants.setForeground(SAT, LOG_COLOR);
            if (sayLogType) {
                doc.insertString(doc.getLength(), "[CONSOLE] : ", SAT);
            }
            doc.insertString(doc.getLength(), text, SAT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // #endregion

    public GamePanel getGamePanel() {
        return gamePanel;
    }

}

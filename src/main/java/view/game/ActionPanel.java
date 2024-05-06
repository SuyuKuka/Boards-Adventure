package view.game;

import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SizeRequirements;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.InlineView;

import controller.Controller;

public final class ActionPanel extends JPanel {

    private static final int HEIGHT_PER = 20;

    private final GamePanel gamePanel;
    private final Controller controller;

    private JComboBox<Object> otherActionBox;
    private JButton rollDiceButton;
    private ArrayList<JPanel> chatPanels = new ArrayList<>();

    public ActionPanel(final GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
        setPreferredSize(new Dimension(gamePanel.getWidth(), gamePanel.getHeight() * HEIGHT_PER / 100));
        setLayout(new GridBagLayout());
        initComponents();
        setOpaque(false);
        setVisible(true);
    }

    private void initComponents() {
        // Ajouts des composants de gauche à droite :
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 0.4;
        add(createTabPanel(), gbc); // log/chat
        gbc.weightx = 0.1;
        add(createButtonsPanel(), gbc); // boutons jouer/autres actions
        gbc.weightx = 0.25;
        add(createInventoryPanel(), gbc); // inventaire cartes, propriétés et items
        add(createMinimapPanel(), gbc); // minimap du plateau
    }

    // #region Chat

    private JTabbedPane createTabPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Global", createChatPanel());
        tabbedPane.add("Systeme", createLogPanel());
        return tabbedPane;
    }

    private JPanel createLogPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        final JTextPane textPane = createTextPane();
        final JScrollPane scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textPane.setPreferredSize(new Dimension(0, 0));
        scrollPane.setPreferredSize(new Dimension(0, 0));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createChatPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        final JTextPane textPane = createTextPane();
        final JScrollPane scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textPane.setPreferredSize(new Dimension(0, 0));
        scrollPane.setPreferredSize(new Dimension(0, 0));
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

    private JTextField createTextField(final JTextPane textPane) {
        final JTextField textField = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !textField.getText().equals("")) {
                    try {
                        final Document doc = textPane.getDocument();
                        doc.insertString(doc.getLength(),
                            controller.getCurrentPlayer().getUsername() + ": ", null);
                        doc.insertString(doc.getLength(), textField.getText() + "\n", null);
                    } catch (BadLocationException bde) {
                        bde.printStackTrace();
                    }
                    textField.setText("");
                }
            }
        });
        return textField;
    }

    // #endregion

    // #region Buttons

    public void playerAction() {
        rollDiceButton.setIcon(new ImageIcon("src/main/resources/images/game/rolling_dice.gif"));
        rollDiceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                controller.rollDice();
                rollDiceButton.removeMouseListener(this);
            }
        });
    }

    private JPanel createButtonsPanel() {
        final JPanel panel = new JPanel(new GridLayout(2, 1));
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

    // #region Inventory

    private JPanel createInventoryPanel() {
        final JPanel panel = new JPanel();
        panel.setBackground(Color.RED);
        return panel;
    }


    // #endregion

    // #region MiniMap

    private JPanel createMinimapPanel() {
        final JPanel panel = new JPanel();
        panel.setBackground(Color.PINK);
        return panel;
    }

    //#endregion
}

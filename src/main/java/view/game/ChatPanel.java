package view.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SizeRequirements;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.InlineView;

public final class ChatPanel extends JPanel {

    private final GamePanel gamePanel;

    private final JTextPane globalTextPane;
    private final JTextPane logTextPane;

    public ChatPanel(final GamePanel gamePanel, final int width, final int height) {
        this.gamePanel = gamePanel;
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());

        globalTextPane = createTextPane();
        logTextPane = createTextPane();

        add(createTabPanel(globalTextPane, logTextPane), BorderLayout.CENTER);

        setVisible(true);
    }

    // #region Chat

    private JTabbedPane createTabPanel(final JTextPane globalPane, final JTextPane logPane) {
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
                    try {
                        final Document doc = textPane.getDocument();
                        doc.insertString(doc.getLength(),
                            gamePanel.getController().getCurrentPlayer().getName() + ": ", null);
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

    public void addPublicText(final String string) {
        final Document doc = globalTextPane.getDocument();
        try {
            doc.insertString(doc.getLength(), string + "\n", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLogText(final String string) {
        final Document doc = logTextPane.getDocument();
        try {
            doc.insertString(doc.getLength(), string + "\n", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // #endregion
}

package view;

import java.util.HashMap;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Controller;
import game.Game;
import view.game.GamePanel;
import view.game.cellsgraphics.CellGraphics;
import view.menu.MenuPanel;

/**
 * Classe mère de la vue qui s'occupe de la fenêtre de l'application et contient un {@link CorePanel}.
 * <p> Lorsque le joueur est dans le menu, un {@link view.menu.MenuPanel} est crée et affecté à {@link #corePanel}.
 * <p> Lorsque le joueur est en jeu, un {@link view.game.GamePanel} est crée et affecté à {@link #corePanel}.
 * <p> La classe mère se charge de redimensionner les éléments que composent {@link #corePanel}.
 *
 * @see CorePanel
 */
public final class View extends JFrame {
    // 720 * 1 ou 1366 * 1.897.
    private static final int MIN_WIDTH = 720;
    // 440 * 0.9 ou 768 * 1.6.
    private static final int MIN_HEIGHT = 440;
    private final Game game;

    private CorePanel corePanel;

    public View(final Game game) {
        this.game = game;
        corePanel = null;
        setTitle("Board's Adventure.");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int screenWidth = (int) screenSize.getWidth();
        final int screenHeight = (int) screenSize.getHeight();
        setPreferredSize(new Dimension(screenWidth / 2, screenHeight / 2));
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setLocationRelativeTo(null);
        pack();
    }

    public void initNewMenuPanel() {
        corePanel = new MenuPanel(this);
        final Container container = getContentPane();
        for (JPanel panel : corePanel.getPanels().values()) {
            panel.setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
        }
        add(corePanel, BorderLayout.CENTER);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Container container = getContentPane();
                corePanel.setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
                for (JPanel panel : corePanel.getPanels().values()) {
                    panel.setBounds(0, 0, container.getWidth(), container.getHeight());
                    panel.setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
                }
            }
        });
        setVisible(true);
    }

    public GamePanel initNewGamePanel(final Controller controller, final HashMap<Integer, CellGraphics> cellsGraphics) {
        corePanel = new GamePanel(this, controller, cellsGraphics);
        final Container container = getContentPane();
        for (JPanel panel : corePanel.getPanels().values()) {
            panel.setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
        }
        add(corePanel, BorderLayout.CENTER);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Container container = getContentPane();
                corePanel.setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
                for (JPanel panel : corePanel.getPanels().values()) {
                    panel.setBounds(0, 0, container.getWidth(), container.getHeight());
                    panel.setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
                }
            }
        });
        setVisible(true);
        revalidate();
        repaint();
        return (GamePanel) corePanel;
    }

    public Game getGame() {
        return game;
    }

    public void removePanel() {
        if (corePanel != null) {
            remove(corePanel);
            revalidate();
            repaint();
        }
    }

    public void setCorePanel(final CorePanel corePanel) {
        this.corePanel = corePanel;
    }

    public boolean hasPanel() {
        return corePanel != null;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }

}

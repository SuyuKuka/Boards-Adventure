package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Controller;
import game.Game;
import view.game.GamePanel;
import view.menu.MenuPanel;

public final class View extends JFrame {

    private static final int MIN_WIDTH = 1360; // 720
    private static final int MIN_HEIGHT = 810; // 440

    private final Game game;
    private CorePanel corePanel;

    public View(final Game game) {
        this.game = game;
        corePanel = null;
        setTitle("Board's Adventure");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setLayout(null);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int screenWidth = (int) screenSize.getWidth();
        final int screenHeight = (int) screenSize.getHeight();
        setPreferredSize(new Dimension(screenWidth / 4, screenHeight / 4));
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setLocation(screenWidth / 8, screenHeight / 8);
        pack();
    }

    public void initNewMenuPanel() {
        corePanel = new MenuPanel(this);
        final Container container = getContentPane();
        for (JPanel panel : corePanel.getPanels().values()) {
            panel.setBounds(0, 0, container.getWidth(), container.getHeight());
            panel.setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
        }
        add(corePanel);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Container container = getContentPane();
                corePanel.setBounds(0, 0, container.getWidth(), container.getHeight());
                corePanel.setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
                for (JPanel panel : corePanel.getPanels().values()) {
                    panel.setBounds(0, 0, container.getWidth(), container.getHeight());
                    panel.setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
                }
            }
        });
        setVisible(true);
    }

    public GamePanel initNewGamePanel(final Controller controller) {
        corePanel = new GamePanel(this, controller);
        final Container container = getContentPane();
        for (JPanel panel : corePanel.getPanels().values()) {
            panel.setBounds(0, 0, container.getWidth(), container.getHeight());
            panel.setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
        }
        add(corePanel);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Container container = getContentPane();
                corePanel.setBounds(0, 0, container.getWidth(), container.getHeight());
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

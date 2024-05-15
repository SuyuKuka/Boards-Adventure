package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.json.JSONArray;

import controller.Controller;
import game.Game;
import view.game.GamePanel;
import view.menu.MenuPanel;

public final class View extends JFrame {

    private static final int MIN_WIDTH = 1366;
    private static final int MIN_HEIGHT = 768;

    private final Game game;
    private JLayeredManager modePanel;

    public View(final Game game) {
        this.game = game;
        modePanel = null;
        setTitle("Board's Adventure");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int screenWidth = (int) screenSize.getWidth();
        final int screenHeight = (int) screenSize.getHeight();
        setPreferredSize(new Dimension(screenWidth / 4, screenHeight / 4));
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setLocationRelativeTo(null);
        pack();
    }

    public void initNewMenuPanel() {
        modePanel = new MenuPanel(this);
        add(modePanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public GamePanel initNewGamePanel(final Controller controller, final JSONArray jCells) {
        modePanel = new GamePanel(this, controller, jCells);
        add(modePanel, BorderLayout.CENTER);
        setVisible(true);
        return (GamePanel) modePanel;
    }

    public void removePanel() {
        if (modePanel != null) {
            remove(modePanel);
            revalidate();
            repaint();
        }
    }

    public Game getGame() {
        return game;
    }

    public void setModePanel(final JLayeredManager modePanel) {
        this.modePanel = modePanel;
    }

    public boolean hasPanel() {
        return modePanel != null;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }

}

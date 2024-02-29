package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import controller.GameController;
import controller.Resources;
import game.Launcher.Mode;
import view.game.GamePanel;
import view.menu.MenuPanel;

public final class GameView extends JFrame {

    private GameController controller;
    private Mode mode;

    private MenuPanel menuPanel;
    private GamePanel gamePanel;

    private int contentPaneWidth;
    private int contentPaneHeight;

    public GameView() {
        setTitle("Kingdom Revolution");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        setPreferredSize(new Dimension(screenWidth / 2, screenHeight / 2));
        setMinimumSize(new Dimension(720, 480));
        setLocation(screenWidth / 4, screenHeight / 4);
        pack();
    }
    public void init() {
        mode = controller.getMode();
        updateContentPaneVariables();
        if (mode == Mode.MENU) {
            menuPanel = new MenuPanel(this);
            add(menuPanel);
        } else {
            gamePanel = new GamePanel(this, Resources.getGameMapGraphics("map4"));
            add(gamePanel);
        }
        // Listeners/Observers
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                updateContentPaneVariables();
                if (mode == Mode.MENU) {
                    menuPanel.updateSize();
                } else if (mode == Mode.GAME) {
                    gamePanel.updateSize();
                }
                revalidate();
            }
        });
        setVisible(true);
    }

    public void updateContentPaneVariables() {
        contentPaneWidth = getContentPane().getWidth();
        contentPaneHeight = getContentPane().getHeight();
    }

    public void render() {
        // System.out.println("view render !");
    }

    public void setMode(final Mode mode) {
        this.mode = mode;
    }

    public int getContentPaneHeight() {
        return contentPaneHeight;
    }

    public int getContentPaneWidth() {
        return contentPaneWidth;
    }

    public void setController(final GameController controller) {
        this.controller = controller;
    }
}

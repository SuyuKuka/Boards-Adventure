package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import controller.MonopolyController;
import controller.MonopolyController.Mode;

public class MonopolyView extends JFrame {

    private MonopolyController controller;
    private Mode mode;

    private MenuPanel menuPanel;
    private GamePanel gamePanel;

    private int contentPaneWidth;
    private int contentPaneHeight;

    public MonopolyView(final MonopolyController controller) {
        this.controller = controller;
        mode = controller.getMode();
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

        updateContentPaneVariables();
        menuPanel = new MenuPanel(this);
        add(menuPanel);
        // Listeners/Observers
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateContentPaneVariables();
                if (mode == Mode.MENU) {
                    menuPanel.updateSize();
                } else if (mode == Mode.GAME) {
                    System.out.println("game");
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
}

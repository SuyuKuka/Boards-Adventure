package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.GameController;
import controller.MenuController;
import view.game.GameMapGraphics;
import view.game.GamePanel;
import view.menu.MenuPanel;

public final class View extends JFrame {

    private CorePanel corePanel;

    public View() {
        corePanel = null;
        setTitle("Kingdom Revolution");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setLayout(null);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int screenWidth = (int) screenSize.getWidth();
        final int screenHeight = (int) screenSize.getHeight();
        setPreferredSize(new Dimension(screenWidth / 2, screenHeight / 2));
        setMinimumSize(new Dimension(720, 480));
        setLocation(screenWidth / 4, screenHeight / 4);
        pack();
    }

    public MenuPanel initNewMenuPanel(final MenuController controller) {
        if (corePanel != null) {
            remove(corePanel);
            revalidate();
            repaint();
        }
        corePanel = new MenuPanel(this, controller);
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
                revalidate();
            }
        });
        setVisible(true);
        return (MenuPanel) corePanel;
    }

    public GamePanel initNewGamePanel(final GameController controller, final GameMapGraphics gameMapGraphics) {
        if (corePanel != null) {
            remove(corePanel);
            revalidate();
            repaint();
        }
        corePanel = new GamePanel(this, controller, gameMapGraphics);
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
                revalidate();
            }
        });
        setVisible(true);
        return (GamePanel) corePanel;
    }

    public void setCorePanel(final CorePanel corePanel) {
        this.corePanel = corePanel;
    }

    public CorePanel getCorePanel() {
        return corePanel;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }

}

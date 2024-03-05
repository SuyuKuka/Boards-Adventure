package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

public final class View extends JFrame {

    private CorePanel corePanel;

    public View() {
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

    public void init(final CorePanel panel) {
        this.corePanel = panel;
        add(corePanel);
        // Listeners/Observers
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                corePanel.resized();
                corePanel.setPreferredSize(getPreferredSize());
                revalidate();
            }
        });
        setVisible(true);
    }

    public CorePanel getCorePanel() {
        return corePanel;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }

}

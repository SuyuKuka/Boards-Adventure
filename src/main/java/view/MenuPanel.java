package view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import game.Resources;

public class MenuPanel extends JLayeredPane implements MenuPanelInterface {

    private MonopolyView frame;

    private JPanel background;
    private JPanel overlay;

    public MenuPanel(final MonopolyView frame) {
        this.frame = frame;
        setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        createBackground();
        createOverlay();
        add(background, new Integer(0));
        add(overlay, new Integer(1));
        setVisible(true);
    }

    public void updateSize() {
        setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        background.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        overlay.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
    }

    public void createBackground() {
        background = new JPanel(new BorderLayout());
        background.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        JLabel bgImg = new JLabel();
        bgImg.setIcon(new ImageIcon(Resources.getImage("logo")));
        background.add(bgImg, BorderLayout.CENTER);
        background.setVisible(true);
    }

    public void createOverlay() {
        overlay = new JPanel(new BorderLayout());
        overlay.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        overlay.setOpaque(false);
        overlay.setVisible(true);

    }
}

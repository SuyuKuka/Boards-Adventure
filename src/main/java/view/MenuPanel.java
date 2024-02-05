package view;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import game.Resources;

public class MenuPanel extends JLayeredPane implements MenuPanelInterface {

    private MonopolyView frame;

    public MenuPanel(final MonopolyView frame) {
        this.frame = frame;
        setBounds(0, 0, frame.getFrameWidth(), frame.getFrameHeight());
        add(createBackground(), new Integer(0));
        add(createOverlay(), new Integer(1));
        repaint();
        setVisible(true);
    }

    public JPanel createBackground() {
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, frame.getFrameWidth(), frame.getFrameHeight());
        panel.setVisible(true);
        
        return panel;
    }

    public JPanel createOverlay() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(0, 0, frame.getFrameWidth(), frame.getFrameHeight());
        panel.setOpaque(false);
 
        JLabel logo = new JLabel("SALUT");
        logo.setIcon(new ImageIcon(Resources.getImage("logo")));
        logo.setHorizontalAlignment(JLabel.CENTER);
        logo.setVerticalAlignment(JLabel.CENTER);

        panel.add(logo, BorderLayout.NORTH);
        panel.setVisible(true);

        return panel;
    }
}

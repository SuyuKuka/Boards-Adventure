package view.game;

import java.util.ArrayList;
import java.util.HashMap;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import view.GameMapGraphics;
import view.MonopolyView;
import view.game.cellsgraphics.CellGraphics;

public final class GamePanel extends JLayeredPane {

    private MonopolyView frame;
    private GameMapGraphics gameMapGraphics;
    private ArrayList<CellGraphics> cellsGraphics = new ArrayList<>();

    public GamePanel(final MonopolyView frame, final GameMapGraphics gameMapGraphics) {
        this.frame = frame;
        this.gameMapGraphics = gameMapGraphics;
        add(createBackground(), Integer.valueOf(0));
        add(createCellsGraphics(), Integer.valueOf(1));
        setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                for (Component comp : getComponents()) {
                    comp.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
                }
                revalidate();
            }
        });
        setVisible(true);
    }

    public void updateSize() {
        setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
    }

    public JPanel createBackground() {
        JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        Image boardImg = gameMapGraphics.getImage("board");
        JLabel backgroundLabel = new JLabel(new ImageIcon(boardImg));
        backgroundLabel.setSize(new Dimension(boardImg.getWidth(null), boardImg.getHeight(null)));
        panel.add(backgroundLabel);
        panel.setVisible(true);
        return panel;
    }

    public JPanel createCellsGraphics() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, frame.getContentPaneWidth(), frame.getContentPaneHeight());
        final HashMap<Integer, CellGraphics> cellsGraphics = gameMapGraphics.getCellsGraphics();
        for (Integer key : cellsGraphics.keySet()) {
            panel.add(cellsGraphics.get(key));
        }
        panel.setVisible(true);
        return panel;
    }

    public JPanel createPropertyCell() {
        final JPanel cell = new JPanel();
        return cell;
    }
}

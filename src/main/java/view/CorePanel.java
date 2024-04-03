package view;

import java.awt.Container;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public abstract class CorePanel extends JLayeredPane {

    private final View frame;
    /** Panels qui compose le {@link JLayeredPane}. Lorsqu'il est resized, les panels aussi le sont. */
    private final HashMap<Integer, JPanel> panels;

    public CorePanel(final View frame) {
        this.frame = frame;
        this.panels = new HashMap<>();
        final Container container = frame.getContentPane();
        setBounds(0, 0, container.getWidth(), container.getHeight());
        setMinimumSize(frame.getMinimumSize());
        setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
    }

    /**
     * Ajoute le panel à la couche layerIndex si possible.
     * @param panel
     * @param layerIndex
     */
    public final void addPanel(final JPanel panel, final int layerIndex) {
        if (!panels.containsKey(layerIndex)) {
            super.add(panel, Integer.valueOf(layerIndex));
            panels.put(layerIndex, panel);
            revalidate();
        }
    }

    /**
     * Retire le panel à la couche layerIndex si elle existe.
     * @param layerIndex
     */
    public final void removePanel(final int layerIndex) {
        if (panels.containsKey(layerIndex)) {
            final JPanel panel = panels.get(layerIndex);
            panel.removeAll();
            panels.remove(layerIndex);
            remove(panel);
            repaint();
        }
    }

    public final HashMap<Integer, JPanel> getPanels() {
        return panels;
    }

    public final View getFrame() {
        return frame;
    }

    @Override
    public final int getWidth() {
        return (int) getPreferredSize().getWidth();
    }

    @Override
    public final int getHeight() {
        return (int) getPreferredSize().getHeight();
    }
}

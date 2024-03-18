package view;

import java.util.HashMap;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public abstract class CorePanel extends JLayeredPane {
    private static final int MAX_PANELS = 10;


    private final View frame;
    /** Panels qui compose le {@link JLayeredPane}. Lorsqu'il est resized, les panels aussi le sont. */
    private final HashMap<Integer, JPanel> panels;

    public CorePanel(final View frame) {
        this.frame = frame;
        this.panels = new HashMap<>();
        setBounds(0, 0, frame.getWidth(), frame.getHeight());
    }

    /**
     * Ajoute le panel à la première couche disponible.
     * @param panel
     */
    public final void add(final JPanel panel) {
        for (int i = 0; i < MAX_PANELS; i++) {
            if (!panels.containsKey(i)) {
                add(panel, i);
                break;
            }
        }
    }

    /**
     * Ajoute le panel à la couche layerIndex si possible.
     * @param panel
     * @param layerIndex
     */
    public final void add(final JPanel panel, final int layerIndex) {
        if (!panels.containsKey(layerIndex)) {
            super.add(panel, Integer.valueOf(layerIndex));
            panels.put(layerIndex, panel);
            revalidate();
        }
    }

    /**
     * Retire le panel à la dernière couche occupée.
     */
    public final void remove() {
        for (int i = MAX_PANELS - 1; i >= 0; i++) {
            if (panels.containsKey(i)) {
                remove(i);
                break;
            }
        }
    }

    /**
     * Retire le panel à la couche layerIndex si elle existe.
     * @param layerIndex
     */
    public final void remove(final int layerIndex) {
        if (panels.containsKey(layerIndex)) {
            super.remove(panels.get(layerIndex));
            panels.remove(layerIndex);
            revalidate();
        }
    }

    /**
     * Méthode appelée à chaque fois que la fenêtre principale a été redimensionnée
     * et redimensionne tous les panels compris dans {@link #panels}.
     */
    public final void resized() {
        setBounds(0, 0, frame.getWidth(), frame.getHeight());
        for (JPanel panel : panels.values()) {
            panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        }
        revalidate();
    }

    public final HashMap<Integer, JPanel> getPanels() {
        return panels;
    }

    public final View getFrame() {
        return frame;
    }

    @Override
    public final int getWidth() {
        return frame.getWidth();
    }

    @Override
    public final int getHeight() {
        return frame.getHeight();
    }
}

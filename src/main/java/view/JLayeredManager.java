package view;

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;

import javax.swing.JLayeredPane;

public abstract class JLayeredManager extends JLayeredPane {

    /** Panels qui compose le {@link JLayeredPane}. Lorsqu'il est resized, les panels aussi le sont. */
    private final HashMap<Integer, Component> layersMap = new HashMap<>();

    /**
     * Ajoute le {@link Component} à la couche layerIndex si possible avec la contrainte.
     * @param comp
     * @param constraints
     * @param layerIndex
     */
    public final void addComp(final Component comp, Object constraints, final int layerIndex) {
        if (!layersMap.containsKey(layerIndex)) {
            super.add(comp, constraints, Integer.valueOf(layerIndex));
            layersMap.put(layerIndex, comp);
            revalidate();
        }
    }

    /**
     * Ajoute le {@link Component} à la couche layerIndex si possible.
     * @param comp
     * @param layerIndex
     */
    public final void addComp(final Component comp, final int layerIndex) {
        if (!layersMap.containsKey(layerIndex)) {
            super.add(comp, Integer.valueOf(layerIndex));
            layersMap.put(layerIndex, comp);
            revalidate();
        }
    }

    /**
     * Retire le panel à la couche layerIndex si elle existe.
     * @param layerIndex
     */
    public final void removePanel(final int layerIndex) {
        if (layersMap.containsKey(layerIndex)) {
            final Component comp = layersMap.get(layerIndex);
            if (comp instanceof Container) {
                ((Container) comp).removeAll();
            }
            layersMap.remove(layerIndex);
            remove(comp);
            repaint();
        }
    }

    public final HashMap<Integer, Component> getLayersMap() {
        return layersMap;
    }

    public final Component getLayerComp(final int index) {
        return layersMap.get(index);
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

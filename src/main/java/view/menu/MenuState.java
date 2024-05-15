package view.menu;

public enum MenuState {
    // BACKGROUND(0)
    MAINMENU(1),
    SETTINGS(2),
    SELECTMOD(3),
    JOINGAME(4),
    MAPCONFIG(5),
    CHARACTER(6),
    // MANUALICON(7)
    // MANUALBOOK(8)
    MANUALBOOK_INDEX(8);

    private final int layerIndex;

    MenuState(final int layerIndex) {
        this.layerIndex = layerIndex;
    }

    public int getLayerIndex() {
        return layerIndex;
    }
}

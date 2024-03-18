package view.menu;

public enum MenuState {
    // BACKGROUND(0)
    MAINMENU(1),
    SETTINGSMENU(2),
    CREATEJOIN(3),
    JOINCODE(4),
    GAMELOBBY(5);
    // MANUALICON(6)
    // MANUALBOOK(7)

    private final int layerIndex;

    MenuState(final int layerIndex) {
        this.layerIndex = layerIndex;
    }

    public int getLayerIndex() {
        return layerIndex;
    }
}

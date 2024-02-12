package view;

import java.awt.Image;

public class GameMapGraphics {
    private final String mapName;
    private final Image boardImage;
    private final CellGraphics[] cellsGraphics;

    public GameMapGraphics(final String mapName, final Image boardImage, final CellGraphics[] cellsGraphics) {
        this.mapName = mapName;
        this.boardImage = boardImage;
        this.cellsGraphics = cellsGraphics;
    }
}

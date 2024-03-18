package view.game;

import java.util.HashMap;

import model.config.GameMap;
import view.game.cellsgraphics.CellGraphics;

public final class GameMapGraphics {

    private static final int CELL_HEIGHT = 25;
    private static final int CELL_WIDTH = 25;

    private final GameMap gameMap;
    private final HashMap<Integer, CellGraphics> cellsGraphics;

    public GameMapGraphics(final GameMap gameMap, final HashMap<Integer, CellGraphics> cellsGraphics) {
        this.gameMap = gameMap;
        this.cellsGraphics = cellsGraphics;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public HashMap<Integer, CellGraphics> getCellsGraphics() {
        return cellsGraphics;
    }

    public CellGraphics getCellGraphics(final int cellID) {
        return cellsGraphics.get(cellID);
    }
}

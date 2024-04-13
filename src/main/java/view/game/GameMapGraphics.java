package view.game;

import java.util.HashMap;

import model.config.GameMap;
import view.game.cellsgraphics.CellGraphics;

/**
 * Classe qui contient toutes les données uniquement graphiques d'une {@link GameMap}.
 * Elle contient :
 * <p> - Une {@link GameMap}.
 * <p> - Des {@link CellGraphics} pour chaque {@link model.config.cells.Cell} du jeu.
 */
public final class GameMapGraphics {

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

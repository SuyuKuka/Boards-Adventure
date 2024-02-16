package model.config;

import java.util.HashMap;

import model.config.cell.Cell;

public final class GameMap {
    private final String mapName;
    private final HashMap<Integer, Cell> cells;

    public GameMap(final String mapName, final HashMap<Integer, Cell> cells) {
        this.mapName = mapName;
        this.cells = cells;
    }

    public String getMapName() {
        return mapName;
    }

    public HashMap<Integer, Cell> getCells() {
        return cells;
    }

    public int maxCells() {
        return cells.size();
    }

    public String toString() {
        return "mapName : " + mapName + ", Nombre de Cells : " + cells.size();
    }
}

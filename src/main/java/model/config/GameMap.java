package model.config;

import model.config.cell.Cell;

public class GameMap {
    private final String mapName;
    private final Cell[] cells;

    public GameMap(final String mapName, final Cell[] cells) {
        this.mapName = mapName;
        this.cells = cells;
    }
}

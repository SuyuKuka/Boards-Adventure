package model.config.cells;

import model.GameLogic;
import model.config.players.Player;

public abstract class Cell {

    private final int id;
    private final CellType cellType;

    public Cell(final int id, final CellType cellType) {
        this.id = id;
        this.cellType = cellType;
    }

    public abstract void playEffect(Player player, GameLogic gameLogic);

    public final int getId() {
        return id;
    }

    public final String toString() {
        return getClass().getSimpleName();
    };

    public final CellType getCellType() {
        return cellType;
    }
}

package model.config.cells;

import model.Model;
import model.Player;

public abstract class Cell {

    private final int id;
    private final boolean shouldPlayerStopBy;
    private final CellType cellType;
    private final int nextCellID;

    public Cell(final int id, final boolean shouldPlayerStopBy, final CellType cellType, final int nextCellID) {
        this.id = id;
        this.shouldPlayerStopBy = shouldPlayerStopBy;
        this.cellType = cellType;
        this.nextCellID = nextCellID;
    }

    public abstract void playCellEffect(Player player, Model model);

    public final int getId() {
        return id;
    }

    public final int getNextCellID() {
        return nextCellID;
    }

    public final String toString() {
        return getClass().getSimpleName();
    };

    public final CellType getCellType() {
        return cellType;
    }

    public final boolean shouldPlayerStopBy() {
        return shouldPlayerStopBy;
    }
}

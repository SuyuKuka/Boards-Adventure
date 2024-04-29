package model.config.cells;

import model.Model;
import model.config.players.Player;

public abstract class Cell {

    private final int id;
    private final boolean shouldPlayerStop;
    private final CellType cellType;
    private final int nextCellID;

    public Cell(final int id, final boolean shouldPlayerStop, final CellType cellType, final int nextCellID) {
        this.id = id;
        this.shouldPlayerStop = shouldPlayerStop;
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

    public final boolean shouldPlayerStop() {
        return shouldPlayerStop;
    }
}

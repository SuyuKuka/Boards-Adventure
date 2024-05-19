package model.config.cells;

import model.Model;
import model.Player;

public abstract class Cell {

    private final int id;
    /** -1:  Le joueur ne s'arrête pas.
     * 0: Le joueur s'arrête et ne consomme par de valeur du dé du joueur.
     * 1: Le joueur s'arrête et consomme 1 points de la valeur du dé du joueur. */
    private final int shouldPlayerStopBy;
    private final CellType cellType;
    private final int nextCellID;
    private boolean isStarCell;

    public Cell(final int id, final int shouldPlayerStopBy, final CellType cellType, final int nextCellID) {
        this.id = id;
        this.shouldPlayerStopBy = shouldPlayerStopBy;
        this.cellType = cellType;
        this.nextCellID = nextCellID;
        this.isStarCell = false;
    }

    public abstract void playCellEffect(Player player, Model model);

    public final void computerBuyStar(final Player player, final Model model) {
        if (!player.isHuman() && player.hasEnoughCoins(model.getStarPrice())) {
            player.buyStar(model.getStarPrice());
            setStarCell(false);
            player.setDiceRollValue(0);
            model.loadRandomStarCell();
        }
    }

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
        return shouldPlayerStopBy == 0 || shouldPlayerStopBy == 1;
    }

    public final int getShouldPlayerStopByValue() {
        return shouldPlayerStopBy;
    }

    public final void setStarCell(final boolean starCell) {
        this.isStarCell = starCell;
    }

    public final boolean isStarCell() {
        return isStarCell;
    }
}

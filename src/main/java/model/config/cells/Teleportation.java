package model.config.cells;

import model.Model;
import model.Player;

public final class Teleportation extends Cell {

    private final int idTargetCell;

    public Teleportation(final int id, final int shouldPlayerStop, final CellType cellType, final int nextCellID,
        final int idTargetCell) {
        super(id, shouldPlayerStop, cellType, nextCellID);
        this.idTargetCell = idTargetCell;
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        if (isStarCell()) {
            computerBuyStar(player, model);
        } else {
            player.setPosition(idTargetCell);
        }
    }

}

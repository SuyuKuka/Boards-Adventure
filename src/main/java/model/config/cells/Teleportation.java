package model.config.cells;

import model.Model;
import model.config.players.Player;

public final class Teleportation extends Cell {

    private final int idTargetCell;

    public Teleportation(final int id, final boolean shouldPlayerStop, final CellType cellType, final int nextCellID,
        final int idTargetCell) {
        super(id, shouldPlayerStop, cellType, nextCellID);
        this.idTargetCell = idTargetCell;
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        player.setPosition(idTargetCell);
        System.out.println("TELEPORTATION A LA CASE " + idTargetCell);
        return;
    }

}

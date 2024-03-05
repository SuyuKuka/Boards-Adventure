package model.config.cells;

import model.Model;
import model.config.players.Player;

public final class Teleportation extends Cell {

    private final int idTargetCell;

    public Teleportation(final int id, final CellType cellType, final int nextCellID, final int idTargetCell) {
        super(id, cellType, nextCellID);
        this.idTargetCell = idTargetCell;
    }

    @Override
    public void playEffect(final Player player, final Model gameLogic) {
        player.setPosition(idTargetCell);
        System.out.println("TELEPORTATION A LA CASE " + idTargetCell);
    }

}

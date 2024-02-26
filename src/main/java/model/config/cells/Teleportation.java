package model.config.cells;

import model.GameLogic;
import model.config.players.Player;

public final class Teleportation extends Cell {

    private final int idTargetCell;

    public Teleportation(final int id, final CellType cellType, final int idTargetCell) {
        super(id, cellType);
        this.idTargetCell = idTargetCell;
    }

    @Override
    public void playEffect(final Player player, final GameLogic gameLogic) {
        player.setPosition(idTargetCell);
        System.out.println("TELEPORTATION A LA CASE " + idTargetCell);
    }

}

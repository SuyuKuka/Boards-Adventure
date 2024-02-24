package model.config.cells;

import model.GameLogic;
import model.config.players.Player;

public final class Teleportation extends Cell {

    private final int idTargetCell;

    public Teleportation(final int id, final int idTargetCell) {
        super(id);
        this.idTargetCell = idTargetCell;
    }

    @Override
    public void playEffect(final Player player, final GameLogic gameState) {
        player.setPosition(idTargetCell);
        System.out.println("TELEPORTATION A LA CASE " + idTargetCell);
    }

}

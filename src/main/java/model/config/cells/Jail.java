package model.config.cells;

import model.Model;
import model.config.players.Player;

public final class Jail extends Cell {

    private static final int JAILTURN = 3;

    private int targetCell;

    public Jail(final int id, final boolean shouldPlayerStop, final CellType cellType,
        final int nextCellID, final int targetCell) {
        super(id, shouldPlayerStop, cellType, nextCellID);
        this.targetCell = targetCell;
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        player.setJailTurn(2);
        player.setPreviousPosition(player.getPosition());
        player.setPosition(targetCell);
    }

}

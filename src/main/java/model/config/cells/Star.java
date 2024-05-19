package model.config.cells;

import model.Model;
import model.Player;

public final class Star extends Cell {

    public Star(final int id, final boolean shouldPlayerStop, final CellType cellType,
        final int nextCellID) {
        super(id, shouldPlayerStop, cellType, nextCellID);
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        player.incrementGamePoint(1);
    }

}

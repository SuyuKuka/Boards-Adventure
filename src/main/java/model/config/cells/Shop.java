package model.config.cells;

import model.Model;
import model.config.players.Player;

public final class Shop extends Cell {

    public Shop(final int id, final boolean shouldPlayerStop, final CellType cellType, final int nextCellID) {
        super(id, shouldPlayerStop, cellType, nextCellID);
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        return;
    }

}

package model.config.cells;

import model.Model;
import model.Player;

public final class None extends Cell {

    public None(final int id, final int shouldPlayerStopBy, final CellType cellType, final int nextCellID) {
        super(id, shouldPlayerStopBy, cellType, nextCellID);
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        return;
    }

}

package model.config.cells;

import model.Model;
import model.config.players.Player;

public final class Void extends Cell {

    public Void(final int id, final CellType cellType, final int nextCellID) {
        super(id, cellType, nextCellID);
    }

    @Override
    public void playEffect(final Player player, final Model gameLogic) {
        return;
    }

}

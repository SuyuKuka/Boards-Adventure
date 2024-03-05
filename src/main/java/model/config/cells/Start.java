package model.config.cells;

import model.Model;
import model.config.players.Player;

public final class Start extends Cell {

    private final int lapGain;

    public Start(final int id, final CellType cellType, final int nextCellID, final int lapGain) {
        super(id, cellType, nextCellID);
        this.lapGain = lapGain;
    }

    @Override
    public void playEffect(final Player player, final Model gameLogic) {
        return;
    }

    public int getLapGain() {
        return lapGain;
    }

}

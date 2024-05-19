package model.config.cells;

import model.Model;
import model.Player;

public final class Start extends Cell {

    private final int lapGain;

    public Start(final int id, final int shouldPlayerStop, final CellType cellType, final int nextCellID,
        final int lapGain) {
        super(id, shouldPlayerStop, cellType, nextCellID);
        this.lapGain = lapGain;
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        player.incrementCoins(lapGain);
        return;
    }

    public int getLapGain() {
        return lapGain;
    }

}

package model.config.cells;

import model.Model;
import model.config.players.Player;

public final class Gain extends Cell {

    private int gain = 0;

    public Gain(final int id, final boolean shouldPlayerStop, final CellType cellType,
        final int nextCellID, final int gain) {
        super(id, shouldPlayerStop, cellType, nextCellID);
        this.gain = gain;
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        player.incrementMoney(gain);
    }

    public int getGain() {
        return gain;
    }

}

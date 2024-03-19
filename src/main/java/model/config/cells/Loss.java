package model.config.cells;

import model.Model;
import model.config.players.Player;

public final class Loss extends Cell {
    private int loss = 0;

    public Loss(final int id, final CellType cellType, final int nextCellID, final int gain) {
        super(id, cellType, nextCellID);
        this.loss = gain;
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        player.getTeam().incrementMoney(-loss);
        return;
    }
}

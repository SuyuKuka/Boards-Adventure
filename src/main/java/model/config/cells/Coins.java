package model.config.cells;

import model.Model;
import model.Player;

public final class Coins extends Cell {

    private final int value;

    public Coins(final int id, final int shouldPlayerStopBy, final CellType cellType,
            final int nextCellID, final int value) {
        super(id, shouldPlayerStopBy, cellType, nextCellID);
        this.value = value;
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        if (isStarCell()) {
            computerBuyStar(player, model);
        } else {
            player.incrementCoins(value);
        }
    }

    public int getValue() {
        return value;
    }

}

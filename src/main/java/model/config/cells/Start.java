package model.config.cells;

import model.GameLogic;
import model.config.players.Player;

public final class Start extends Cell {

    private final int lapGain;

    public Start(final int id, final CellType cellType, final int lapGain) {
        super(id, cellType);
        this.lapGain = lapGain;
    }

    @Override
    public void playEffect(final Player player, final GameLogic gameLogic) {
        return;
    }

    public int getLapGain() {
        return lapGain;
    }

}

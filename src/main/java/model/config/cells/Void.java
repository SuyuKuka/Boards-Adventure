package model.config.cells;

import model.GameLogic;
import model.config.players.Player;

public final class Void extends Cell {

    public Void(final int id, final CellType cellType) {
        super(id, cellType);
    }

    @Override
    public void playEffect(final Player player, final GameLogic gameLogic) {
        return;
    }

}

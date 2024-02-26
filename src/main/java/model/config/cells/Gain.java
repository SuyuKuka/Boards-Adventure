package model.config.cells;

import model.GameLogic;
import model.config.players.Player;

public final class Gain extends Cell {

    private int gain = 0;

    public Gain(final int id, final CellType cellType, final int gain) {
        super(id, cellType);
        this.gain = gain;
    }

    @Override
    public void playEffect(final Player player, final GameLogic gameLogic) {
        player.getTeam().incrementMoney(gain);
        return;
    }
}

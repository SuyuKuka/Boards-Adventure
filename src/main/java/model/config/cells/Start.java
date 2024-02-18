package model.config.cells;

import model.GameState;
import model.config.players.Player;

public final class Start extends Cell {

    private final int lapGain;

    public Start(final int id, final int lapGain) {
        super(id);
        this.lapGain = lapGain;
    }

    @Override
    public void playEffect(final Player player, final GameState gameState) {
        return;
    }

    public int getLapGain() {
        return lapGain;
    }

}

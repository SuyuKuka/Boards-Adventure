package model.config.cells;

import model.GameData;
import model.config.players.Player;

public final class Start extends Cell {

    private final int lapGain;

    public Start(final int id, final int lapGain) {
        super(id);
        this.lapGain = lapGain;
    }

    @Override
    public void playEffect(final Player player, final GameData gameState) {
        return;
    }

    public int getLapGain() {
        return lapGain;
    }

}

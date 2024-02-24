package model.config.cells;

import model.GameData;
import model.config.players.Player;

public final class Gain extends Cell {

    private int gain = 0;

    public Gain(final int id, final int gain) {
        super(id);
        this.gain = gain;
    }

    @Override
    public void playEffect(final Player player, final GameData gameState) {
        player.getTeam().incrementMoney(gain);
        return;
    }
}

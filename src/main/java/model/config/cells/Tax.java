package model.config.cells;

import model.GameState;
import model.config.players.Player;

public final class Tax extends Cell {

    private final int tax;

    public Tax(final int id, final int tax) {
        super(id);
        this.tax = tax;
    }
    @Override
    public void playEffect(final Player player, final GameState gameState) {
        player.incrementMoney(-tax);
    }

}

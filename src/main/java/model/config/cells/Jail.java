package model.config.cells;

import model.GameState;
import model.config.players.Player;

public final class Jail extends Cell {

    private boolean isWaitingCell;

    public Jail(final int id, final boolean isWaitingCell) {
        super(id);
        this.isWaitingCell = isWaitingCell;
    }

    @Override
    public void playEffect(final Player player, final GameState gameState) {
        player.incrementJailTurn(3);
    }

}

package model.config.cells;

import model.GameLogic;
import model.config.players.Player;
import model.config.players.PlayerState;

public final class Jail extends Cell {

    private static final int JAILTURN = 3;

    private int targetCell;

    public Jail(final int id, final int targetCell) {
        super(id);
        this.targetCell = targetCell;
    }

    @Override
    public void playEffect(final Player player, final GameLogic gameState) {
        player.setPlayerState(PlayerState.JAILED);
        player.setJailTurn(JAILTURN);
        player.setPreviousPosition(player.getPosition());
        player.setPosition(targetCell);
    }

}

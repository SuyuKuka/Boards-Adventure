package model.config.cells;

import model.GameState;
import model.Model;
import model.config.players.Player;

public final class Loss extends Cell {
    private final int loss;

    public Loss(final int id, final CellType cellType, final int nextCellID, final int loss) {
        super(id, cellType, nextCellID);
        this.loss = loss;
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        player.getTeam().incrementMoney(-loss);
        model.setGameState(GameState.LOSS);
    }
}

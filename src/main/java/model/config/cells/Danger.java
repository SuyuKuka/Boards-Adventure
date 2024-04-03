package model.config.cells;

import model.GameState;
import model.Model;
import model.config.players.Player;

public final class Danger extends Cell {

    public Danger(final int id, final CellType cellType, final int nextCellID) {
        super(id, cellType, nextCellID);
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        System.out.println(getCellType());
        model.setGameState(GameState.DANGER);
    }
}

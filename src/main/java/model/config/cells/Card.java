package model.config.cells;

import model.Model;
import model.config.players.Player;

public final class Card extends Cell {

    public Card(final int id, final CellType cellType, final int nextCellID) {
        super(id, cellType, nextCellID);
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        System.out.println(getCellType());
    }
}

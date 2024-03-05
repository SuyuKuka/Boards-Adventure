package model.config.cells;

import model.Model;
import model.config.players.Player;

public final class Shop extends Cell {

    public Shop(final int id, final CellType cellType, final int nextCellID) {
        super(id, cellType, nextCellID);
    }

    @Override
    public void playEffect(final Player player, final Model gameLogic) {
        System.out.println("shop ! (pas fini :c");
    }

}

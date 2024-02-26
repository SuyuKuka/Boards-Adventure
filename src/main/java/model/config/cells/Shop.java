package model.config.cells;

import model.GameLogic;
import model.config.players.Player;

public final class Shop extends Cell {

    public Shop(final int id, final CellType cellType) {
        super(id, cellType);
    }

    @Override
    public void playEffect(final Player player, final GameLogic gameLogic) {
        System.out.println("shop ! (pas fini :c");
    }

}

package model.config.cells;

import model.GameLogic;
import model.config.players.Player;

public final class Card extends Cell {

    public Card(final int id, final CellType cellType) {
        super(id, cellType);
    }

    @Override
    public void playEffect(final Player player, final GameLogic gameLogic) {
        System.out.println(getCellType());
    }
}

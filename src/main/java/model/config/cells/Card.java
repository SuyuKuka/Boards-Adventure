package model.config.cells;

import model.GameState;
import model.config.players.Player;

public final class Card extends Cell {

    private CellType cardType;

    public Card(final int id, final CellType cardType) {
        super(id);
        this.cardType = cardType;
    }

    @Override
    public void playEffect(final Player player, final GameState gameState) {
        System.out.println(cardType);
    }
}

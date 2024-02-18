package model.config.cells;

import model.GameState;
import model.config.players.Player;

public abstract class Cell {

    private final int id;

    public Cell(final int id) {
        this.id = id;
    }

    public abstract void playEffect(Player player, GameState gameState);

    public final int getId() {
        return id;
    }

    public final String toString() {
        return getClass().getSimpleName();
    };
}

package model.config.cell;

import model.config.players.Players;

public abstract class Cell {

    private final int id;

    public Cell(final int id) {
        this.id = id;
    }

    public abstract void play(final Players players); 
}

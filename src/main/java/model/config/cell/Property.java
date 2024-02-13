package model.config.cell;

import model.config.players.Player;

public class Property extends Cell {

    private final int group;
    private Player owner;

    public Property(final int id, final int price, final int group) {
        super(id, price);
        this.group = group;
    }
}

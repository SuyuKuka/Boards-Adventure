package model.config.cell;

import java.util.ArrayList;

import model.config.players.Player;
import model.config.players.Players;

public class Property extends Cell {

    private final ArrayList<Integer> buyPrices;
    private final int group;
    private Player owner;

    public Property(final int id, final ArrayList<Integer> buyPrices, final int group) {
        super(id);
        this.buyPrices = buyPrices;
        this.group = group;
    }

    @Override
    public void play(Players players) {
        throw new UnsupportedOperationException("Unimplemented method 'play'");
    }
}

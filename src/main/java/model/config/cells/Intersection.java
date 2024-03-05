package model.config.cells;

import java.util.ArrayList;
import java.util.Random;

import model.Model;
import model.config.players.Player;

public final class Intersection extends Cell {
    private static final Random RAND = new Random();

    private final ArrayList<Integer> nextCellIDs;

    public Intersection(final int id, final CellType cellType, final ArrayList<Integer> nextCellIDs) {
        super(id, cellType, id);
        this.nextCellIDs = nextCellIDs;
    }

    public ArrayList<Integer> getNextCellIDs() {
        return nextCellIDs;
    }

    @Override
    public void playEffect(final Player player, final Model gameLogic) {
        player.setPosition(nextCellIDs.get(RAND.nextInt(nextCellIDs.size())));
    }

}

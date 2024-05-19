package model.config.cells;

import java.util.ArrayList;
import java.util.Random;

import model.Model;
import model.Player;

public final class Intersection extends Cell {

    private final ArrayList<Integer> nextCellIDs;

    public Intersection(final int id, final boolean shouldPlayerStop, final CellType cellType,
        final ArrayList<Integer> nextCellIDs) {
        super(id, shouldPlayerStop, cellType, id);
        this.nextCellIDs = nextCellIDs;
    }

    public ArrayList<Integer> getNextCellIDs() {
        return nextCellIDs;
    }

    public int getRandomNextCellID() {
        return nextCellIDs.get(new Random().nextInt(nextCellIDs.size()));
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        return;
    }

}

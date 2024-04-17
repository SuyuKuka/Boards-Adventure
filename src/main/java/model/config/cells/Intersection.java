package model.config.cells;

import java.util.ArrayList;

import model.Model;
import model.config.players.Player;

public final class Intersection extends Cell {

    private final ArrayList<Integer> nextCellIDs;

    public Intersection(final int id, final CellType cellType, final ArrayList<Integer> nextCellIDs) {
        super(id, cellType, id);
        this.nextCellIDs = nextCellIDs;
    }

    public ArrayList<Integer> getNextCellIDs() {
        return nextCellIDs;
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        return;
    }

}

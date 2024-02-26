package model.config.cells;

import java.util.ArrayList;
import java.util.Random;

import model.GameLogic;
import model.config.players.Player;

public final class Intersection extends Cell {
    private static final Random RAND = new Random();

    private final ArrayList<Integer> intersectID;

    public Intersection(final int id, final CellType cellType, final ArrayList<Integer> intersectID) {
        super(id, cellType);
        this.intersectID = intersectID;
    }

    @Override
    public void playEffect(final Player player, final GameLogic gameLogic) {
        player.setPosition(intersectID.get(RAND.nextInt(intersectID.size())));
    }

}

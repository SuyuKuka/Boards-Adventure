package model.config.cells;

import model.GameData;
import model.config.players.Player;

public final class Void extends Cell {

    public Void(final int id) {
        super(id);
    }

    @Override
    public void playEffect(final Player player, final GameData gameState) {
        return;
    }

}

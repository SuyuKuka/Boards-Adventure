package model.config.cells;

import model.Model;
import model.config.players.Player;

public class Card extends Cell {


    public Card(final int id, final boolean shouldPlayerStop, final CellType cellType, final int nextCellID) {
        super(id, shouldPlayerStop, cellType, nextCellID);
    }

    /**
     * Selectionne aléatoirement une carte dans cards et l'utilise.
     *
     * @param  player  the player to apply the effect to
     * @param  model   the game model
     */
    @Override
    public void playCellEffect(Player player, Model model) {
        // Random rand = new Random();
        // int i = rand.nextInt(cards.size());
        // cards.get(i).applyEffect(player, model);
        // model.setGameState(GameState.CARD);
    }

}

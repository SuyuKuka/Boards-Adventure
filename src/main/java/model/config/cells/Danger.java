package model.config.cells;

import model.Model;
import model.Player;

public final class Danger extends Cell {

    public Danger(final int id, final int shouldPlayerStop, final CellType cellType, final int nextCellID) {
        super(id, shouldPlayerStop, cellType, nextCellID);
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        if (isStarCell()) {
            computerBuyStar(player, model);
        }
        player.setCoins(1);
    }


// Changement d'équipe entre joueur
// swap de propriétés entre équipes
// swap de points et argents entre joueurs et équipe
// swap d'items de départ entre tout le monde
// HUD désactivé (les pts, l'argent, le nombre de prop. est remplacé par des ??? et
// les propriétés sur la map perdent leurs couleurs)
}

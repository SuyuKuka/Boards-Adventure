package model.config.cells;

import model.Model;
import model.config.players.Player;

public final class Kingdom extends Cell {

    private final int targetCell;

    public Kingdom(final int id, final boolean shouldPlayerStop, final CellType cellType,
        final int nextCellID, final int targetCell) {
        super(id, shouldPlayerStop, cellType, nextCellID);
        this.targetCell = targetCell;
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        // if (player.getMapLap() <= 0) {
        //     System.out.println(player.getUsername()
        //         + " a essayé de devenir roi mais n'a pas encore fait le tour de la map !");
        // } else if (model.getCurrentLeader() == null) {
        //     model.changeLeader(player);
        //     player.setPosition(targetCell);
        //     System.out.println(player.getUsername() + " est devenu Roi !");
        // } else {
        //     final int tax = player.getTeam().getMoney() / 2;
        //     final Player currentKing = model.getCurrentLeader();
        //     currentKing.getTeam().incrementMoney(tax);
        //     player.getTeam().incrementMoney(-tax);
        //     System.out.println(player.getUsername() + " a donné " + tax + "$ au roi " + currentKing
        //         + " ! Longue vie au Roi !");
        // }
        player.incrementGamePoint(1);
    }

}

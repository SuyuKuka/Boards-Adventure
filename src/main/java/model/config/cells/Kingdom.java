package model.config.cells;

import model.GameState;
import model.config.players.Player;

public final class Kingdom extends Cell {
    private final int targetCell;
    public Kingdom(final int id, final int targetCell) {
        super(id);
        this.targetCell = targetCell;
    }

    @Override
    public void playEffect(final Player player, final GameState gameState) {
        if (player.getMapTurn() <= 0) {
            System.out.println(player.getUsername()
                + " a essayé de devenir roi mais n'a pas encore fait le tour de la map !");
        } else if (gameState.getCurrentKing() == null) {
            gameState.changeKing(player);
            player.setPosition(targetCell);
            System.out.println(player.getUsername() + " est devenu Roi !");
        } else {
            final int tax = player.getMoney() / 2;
            final Player currentKing = gameState.getCurrentKing();
            currentKing.incrementMoney(tax);
            player.incrementMoney(-tax);
            System.out.println(player.getUsername() + " a donné " + tax + "$ au roi " + currentKing
                + " ! Longue vie au Roi !");
        }
    }

}

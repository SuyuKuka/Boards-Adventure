package model.config.cells;

import java.util.ArrayList;

import model.GameState;
import model.Model;
import model.config.players.Player;

public final class Property extends Cell {

    private final ArrayList<Integer> buyPrices;
    private final int group;
    private int propertyState = 0;
    private Player owner = null;

    public Property(final int id, final CellType cellType, final int nextCellID,
        final ArrayList<Integer> buyPrices, final int group) {
        super(id, cellType, nextCellID);
        this.buyPrices = buyPrices;
        this.group = group;
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        if (owner == null) {
            if (player.getTeam().getMoney() < buyPrices.get(0)) {
                System.out.println("Désolé, vous n'avez pas assez d'argents pour acheter cette propriété ("
                + buyPrices.get(0) + "$).");
                model.setGameState(GameState.END_TURN);
            } else {
                model.setGameState(GameState.ASK_PROPERTY);
            }
        } else if (isAtHome(player)) {
            if (propertyState + 1 > buyPrices.size()) {
                System.out.println("Votre propriété est déjà au max.");
                model.setGameState(GameState.END_TURN);
            } else if (player.getTeam().getMoney() < buyPrices.get(propertyState + 1)) {
                System.out.println("Désolé, vous n'avez pas assez d'argents pour améliorer votre propriété : ("
                + buyPrices.get(propertyState + 1) + "$).");
                model.setGameState(GameState.END_TURN);
            } else {
                model.setGameState(GameState.ASK_PROPERTY);
            }
        } else {
            final int price = buyPrices.get(propertyState - 1);
            if (player.getTeam().getMoney() < price) {
                //model.removePlayer(player);
                System.out.println(player + " a pas assez d'argents.");
            } else {
                player.getTeam().incrementMoney(-price);
                owner.getTeam().incrementMoney(price);
            }
            model.setGameState(GameState.END_TURN);
        }
    }

    public void buy(final Player player) {
        owner = player;
        player.getTeam().addProperty(this);
        player.getTeam().incrementMoney(-buyPrices.get(propertyState));
        propertyState++;
    }

    public void upgrade(final Player player) {
        player.getTeam().incrementMoney(-buyPrices.get(propertyState));
        propertyState++;
    }

    public boolean isAtHome(final Player player) {
        return player == owner;
    }

    public boolean hasOwner() {
        return this.owner != null;
    }

    public void setOwner(final Player player) {
        this.owner = player;
    }

    public void setPropertyState(int propertyState) {
        this.propertyState = propertyState;
    }

    public int getPropertyState() {
        return propertyState;
    }

    public ArrayList<Integer> getBuyPrices() {
        return buyPrices;
    }

    public Player getOwner() {
        return owner;
    }

    public int getGroup() {
        return group;
    }

}

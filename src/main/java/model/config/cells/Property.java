package model.config.cells;

import java.util.ArrayList;

import model.Model;
import model.config.players.Player;

public final class Property extends Cell {

    private final ArrayList<Integer> buyPrices;
    private final int group;
    private int propertyState = 0;
    private Player owner = null;

    public Property(final int id, final boolean shouldPlayerStop, final CellType cellType, final int nextCellID,
        final ArrayList<Integer> buyPrices, final int group) {
        super(id, shouldPlayerStop, cellType, nextCellID);
        this.buyPrices = buyPrices;
        this.group = group;
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        if (owner == null) {
            if (player.getCoins() < buyPrices.get(0)) {
                System.out.println("Désolé, vous n'avez pas assez d'argents pour acheter cette propriété ("
                + buyPrices.get(0) + "$).");
            }
        } else if (isAtHome(player)) {
            if (propertyState + 1 > buyPrices.size()) {
                System.out.println("Votre propriété est déjà au max.");
            } else if (player.getCoins() < buyPrices.get(propertyState + 1)) {
                System.out.println("Désolé, vous n'avez pas assez d'argents pour améliorer votre propriété : ("
                + buyPrices.get(propertyState + 1) + "$).");
            }
        } else {
            final int price = buyPrices.get(propertyState - 1);
            if (player.getCoins() < price) {
                //model.removePlayer(player);
                System.out.println(player + " a pas assez d'argents.");
            } else {
                player.incrementCoins(-price);
                owner.incrementCoins(price);
            }
        }
    }

    public void buy(final Player player) {
        owner = player;
        player.addProperty(this);
        player.incrementCoins(-buyPrices.get(propertyState));
        propertyState++;
    }

    public void upgrade(final Player player) {
        player.incrementCoins(-buyPrices.get(propertyState));
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

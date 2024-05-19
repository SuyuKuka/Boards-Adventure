package model.config.cells;

import java.util.ArrayList;

import model.Model;
import model.Player;

public final class Property extends Cell {

    private final ArrayList<Integer> buyPrices;
    private final String groupHexColor;
    private int propertyState = 0;
    private Player owner = null;

    public Property(final int id, final int shouldPlayerStop, final CellType cellType, final int nextCellID,
        final ArrayList<Integer> buyPrices, final String groupHexColor) {
        super(id, shouldPlayerStop, cellType, nextCellID);
        this.buyPrices = buyPrices;
        this.groupHexColor = groupHexColor;
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        if (isStarCell()) {
            computerBuyStar(player, model);
            return;
        }
        if (owner != null && !isAtHome(player) && !owner.getHexTeamColor().equals(player.getHexTeamColor())) {
            final int price = (int) (buyPrices.get(propertyState - 1) * 0.70);
            final int leftCoins = player.getCoins();
            if (leftCoins < price) {
                player.incrementCoins(-leftCoins);
                owner.incrementCoins(leftCoins);
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

    public String getGroupHexColor() {
        return groupHexColor;
    }

}

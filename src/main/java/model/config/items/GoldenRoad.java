package model.config.items;
import Random.RANDNextint;
import model.Model;

public final class GoldenRoad extends Item {

    public GoldenRoad(final ItemName itemName, final boolean isShopItem, final int value) {
        super(itemName, isShopItem, value);
    }

    @Override
    public void playItemEffect(final Model model) {
        int gain = RANDNextint(1000)+1;
        model.getCurrentPlayer().incrementMoney(gain);
        model.getCurrentPlayer().getShopItem().remove(this);
    }
}

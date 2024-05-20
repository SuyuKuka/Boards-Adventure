package model.config.cells;

import java.util.Random;

import model.Model;
import model.Player;
import model.config.items.Item;

public final class Shop extends Cell {

    private Item[] shopItem = new Item[3];

    public Shop(final int id, final int shouldPlayerStop, final CellType cellType, final int nextCellID) {
        super(id, shouldPlayerStop, cellType, nextCellID);
    }

    @Override
    public void playCellEffect(final Player player, final Model model) {
        if (!player.isHuman()) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            Random rand = new Random();
            switch (rand.nextInt(7)) {
                case 0:
                    shopItem[i] = model.getGameMap().generateItem("FARMER_HOE", true);
                    break;
                case 1:
                    shopItem[i] = model.getGameMap().generateItem("FARMER_HOE", true);
                    break;
                case 2:
                    shopItem[i] = model.getGameMap().generateItem("SACRED_BOOK", true);
                    break;
                case 3:
                    shopItem[i] = model.getGameMap().generateItem("MAGIC_WAND", true);
                    break;
                case 4:
                    shopItem[i] = model.getGameMap().generateItem("LUCKY_POTION", true);
                    break;
                case 5:
                    shopItem[i] = model.getGameMap().generateItem("BIG_DICE", true);
                    break;
                case 6:
                    shopItem[i] = model.getGameMap().generateItem("GOLDEN_ROAD", true);
                    break;
                default:
                    break;
            }
        }
    }

    public void buyItem(final Player player, Item item) {
        player.addShopItem(item);
    }

    public Item[] getShopItem() {
        return shopItem;
    }

}

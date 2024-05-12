package model.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import model.config.cardseffect.CardEffect;
import model.config.cells.Cell;
import model.config.cells.CellType;
import model.config.cells.Start;
import model.config.items.Item;
import model.config.items.ItemFactory;

/**
 * Classe qui contient toutes les données "logiques" d'une map sous forme de fichier "config.json"
 * trouvable dans "src/resources/maps/{@link #mapName}".
 * <p> Une map contient :
 * <p> - Les {@link Cell} qui composent le plateau de la map.
 * <p> - Une liste de cartes ayant des effets {@link CardEffect}.
 */
public final class GameMap {

    private final String mapName;

    private final ArrayList<CardEffect> cardEffects;
    private int cardEffectsCounter;

    private final HashMap<Integer, Cell> cells;
    private final Start startCell;

    private final HashMap<String, HashMap<String, String>> availableStartItems;
    private final HashMap<String, HashMap<String, String>> availableShopItems;

    public GameMap(final String mapName, final ArrayList<CardEffect> cardEffects, final HashMap<Integer, Cell> cells,
            final HashMap<String, HashMap<String, String>> availableStartItems,
            final HashMap<String, HashMap<String, String>> availableShopItems) {
        this.mapName = mapName;

        this.cardEffects = cardEffects;
        cardEffectsCounter = countCardEffectsQuantity();

        this.cells = cells;
        this.startCell = findStartCell();

        this.availableStartItems = availableStartItems;
        this.availableShopItems = availableShopItems;
    }

    private int countCardEffectsQuantity() {
        int counter = 0;
        for (int i = 0; i < cardEffects.size(); i++) {
            counter += cardEffects.get(i).getQuantity();
        }
        return counter;
    }

    private Start findStartCell() {
        for (Cell cell : cells.values()) {
            if (cell.getCellType() == CellType.START) {
                return (Start) cell;
            }
        }
        return null;
    }

    public CardEffect loadRandomCardEffect() {
        final Random rand = new Random();
        int randInt = rand.nextInt(cardEffectsCounter);
        CardEffect cardEffect = null;
        for (int i = 0; i < cardEffects.size(); i++) {
            cardEffect = cardEffects.get(i);
            if (randInt <= 0) {
                break;
            }
            randInt -= cardEffect.getQuantity();
        }
        cardEffect.decreaseQuantity();
        if (cardEffect.getQuantity() == 0) {
            cardEffects.remove(cardEffect);
        }
        cardEffectsCounter--;
        return cardEffect;
    }

    public String getMapName() {
        return mapName;
    }

    public Cell getCell(final int pos) {
        return cells.get(pos);
    }

    public ArrayList<CardEffect> getCardEffects() {
        return cardEffects;
    }

    public Item generateItem(final String itemName, final boolean isShopItem) {
        if (isShopItem) {
            return ItemFactory.createItem(availableShopItems.get(itemName), true);
        } else {
            return ItemFactory.createItem(availableStartItems.get(itemName), false);
        }
    }

    public Item generateRandomItem(final boolean isShopItem) {
        if (isShopItem) {
            final int rand = new Random().nextInt(availableShopItems.size());
            return generateItem((String) availableShopItems.keySet().toArray()[rand], true);
        } else {
            final int rand = new Random().nextInt(availableStartItems.size());
            return generateItem((String) availableStartItems.keySet().toArray()[rand], false);
        }
    }

    public boolean hasAvailableShopItem() {
        return availableShopItems.size() > 0;
    }

    public boolean hasAvailableStartItem() {
        return availableStartItems.size() > 0;
    }

    public Start getStartCell() {
        return startCell;
    }

}

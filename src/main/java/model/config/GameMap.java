package model.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import model.config.cardseffect.CardEffect;
import model.config.cardseffect.CardEffectFactory;
import model.config.cells.Cell;
import model.config.cells.CellFactory;
import model.config.cells.CellType;
import model.config.cells.Property;
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

    public GameMap(final String mapName, final JSONObject mapConfig) {
        this.mapName = mapName;

        this.cardEffects = loadCardEffects(mapConfig.getJSONArray("cards_effect"));
        cardEffectsCounter = countCardEffectsQuantity();

        this.cells = loadCells(mapConfig.getJSONArray("cells"));
        this.startCell = findStartCell();

        this.availableStartItems = loadAvailableStartItems(mapConfig.getJSONArray("start_items"));
        this.availableShopItems = loadAvailableShopItems(mapConfig.getJSONArray("shop_items"));
    }

    public HashMap<Integer, ArrayList<Property>> getPropertiesGroup() {
        final HashMap<Integer, ArrayList<Property>> map = new HashMap<>();
        for (Cell cell : cells.values()) {
            if (cell instanceof Property) {
                final Property property = (Property) cell;
                final int group = property.getGroup();
                if (!map.containsKey(group)) {
                    map.put(group, new ArrayList<>());
                }
                map.get(group).add(property);
            }
        }
        return map;
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

    //#region load

        public static HashMap<Integer, Cell> loadCells(final JSONArray jCells) {
        final HashMap<Integer, Cell> cells = new HashMap<>();
        for (int i = 0; i < jCells.length(); i++) {
            final JSONObject jCell = jCells.getJSONObject(i);
            cells.put(jCell.getInt("id"), CellFactory.createCell(jCell));
        }
        return cells;
    }

    public static ArrayList<CardEffect> loadCardEffects(final JSONArray jCardsEffect) {
        final ArrayList<CardEffect> cards = new ArrayList<>();
        for (int i = 0; i < jCardsEffect.length(); i++) {
            cards.addAll(CardEffectFactory.createCardEffect(jCardsEffect.getJSONObject(i)));
        }
        return cards;
    }

    public static HashMap<String, HashMap<String, String>> loadAvailableStartItems(final JSONArray jStartItems) {
        final HashMap<String, HashMap<String, String>> startItems = new HashMap<>();
        for (int i = 0; i < jStartItems.length(); i++) {
            final JSONObject jStartItem = jStartItems.getJSONObject(i);
            final HashMap<String, String> itemConfig = new HashMap<>();
            final String itemName = jStartItem.getString("name");
            itemConfig.put("name", itemName);
            itemConfig.put("turnCooldown", jStartItem.getString("turnCooldown"));
            startItems.put(itemName, itemConfig);
        }
        return startItems;
    }

    public static HashMap<String, HashMap<String, String>> loadAvailableShopItems(final JSONArray jShopItems) {
        final HashMap<String, HashMap<String, String>> shopItems = new HashMap<>();
        for (int i = 0; i < jShopItems.length(); i++) {
            final JSONObject jStartItem = jShopItems.getJSONObject(i);
            final HashMap<String, String> itemConfig = new HashMap<>();
            final String itemName = jStartItem.getString("name");
            itemConfig.put("name", itemName);
            itemConfig.put("buyPrice", jStartItem.getString("buyPrice"));
            shopItems.put(itemName, itemConfig);
        }
        return shopItems;
    }

    //#endregion

}

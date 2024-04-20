package model.config.items;

import java.util.ArrayList;

public class Item {

    private ArrayList<Item> startItems = new ArrayList<>();
    private static ArrayList<Item> shopItems = new ArrayList<>();

    private String name;
    private ItemEffect effect;

    public Item(final String name, final ItemEffect effect) {
        //name sera sous la forme de "nom_type", true = start, false = shop
        if (!name.equals("tmp")) {
            this.effect = effect;
            int index = name.indexOf('_');
            if (index != -1) {
                this.name = name.substring(0, index);
                String check = name.substring(index + 1);
                if (check.contains("start")) {
                    createItems(true);
                } else {
                    createItems(false);
                }
            }
        }
    }

    /**
     * La fonction va créer la liste des items de départs et les items du shop.
     * @param type Détermine le type de l'objet permettant de l'insérer dans la bonne liste.
     */
    public void createItems(boolean type) {
        if (type) {
            startItems.add(this);
        } else {
            shopItems.add(this);
        }
    }

    // #region setter / getter

    public final String getName() {
        return name;
    }

    public final ItemEffect getEffect() {
        return effect;
    }

    public final void setEffect(final ItemEffect effect) {
        this.effect = effect;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final ArrayList<Item> getShopItems() {
        return shopItems;
    }

    // #endregion

}

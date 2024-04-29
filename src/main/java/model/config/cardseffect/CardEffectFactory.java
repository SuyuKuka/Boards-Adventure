package model.config.cardseffect;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Génére les cartes à effets {@link CardEffect} du jeu.
 */
public final class CardEffectFactory {

    public static ArrayList<CardEffect> createCardEffect(final JSONObject jCardEffect) {
        final ArrayList<CardEffect> list = new ArrayList<>();
        final CardEffectType cardEffectType = CardEffectType.valueOf(jCardEffect.getString("type").toUpperCase());
        final Integer quantity = jCardEffect.optIntegerObject("quantity", 1);
        switch (cardEffectType) {
            case TELEPORTATION_EFFECT:
                final JSONArray cellIDsList = jCardEffect.getJSONArray("cellIDs");
                for (int i = 0; i < cellIDsList.length(); i++) {
                    list.add(new TeleportationEffect(1, cellIDsList.getInt(i)));
                }
                break;
            case MONEY_EFFECT:
                final JSONArray values = jCardEffect.getJSONArray("quantity-amount");
                for (int i = 0; i < values.length(); i++) {
                    final JSONArray value = values.getJSONArray(i);
                    list.add(new MoneyEffect(value.getInt(0), value.getInt(1)));
                }
                break;
            case LOSE_PROPERTY_EFFECT:
                list.add(new LossPropertyEffect(quantity));
                break;
            case SWAP_POSITION_EFFECT:
                list.add(new SwapPositionEffect(quantity));
                break;
            case GAIN_ITEM_EFFECT:
                list.add(new GainItemEffect(quantity));
                break;
            case LOSE_ITEM_EFFECT:
                list.add(new LoseItemEffect(quantity));
                break;
            case GAIN_GAMEPOINT_EFFECT:
                list.add(new GainGamePointEffect(quantity));
                break;
            default:
                break;
        }
        return list;
    }

}

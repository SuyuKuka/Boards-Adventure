package model.config.cardeffects;

import org.json.JSONObject;

/**
 * Génére les cartes à effets {@link CardEffect} du jeu.
 */
public final class CardEffectFactory {

    public static CardEffect createCardEffect(final JSONObject jCardEffect) {
        final CardEffectType cardEffectType = CardEffectType.valueOf(jCardEffect.getString("type").toUpperCase());
        final Integer quantity = jCardEffect.optIntegerObject("quantity", 1);
        switch (cardEffectType) {
            case TELEPORTATION_EFFECT:
                return new TeleportationEffect(quantity, jCardEffect.getInt("cellID"));
            case MONEY_EFFECT:
                return new MoneyEffect(quantity, jCardEffect.getInt("amount"));
            case LOSE_PROPERTY_EFFECT:
                return new LossPropertyEffect(quantity);
            case SWAP_POSITION_EFFECT:
                return new SwapPositionEffect(quantity);
            case GAIN_ITEM_EFFECT:
                return new GainItemEffect(quantity);
            case LOSE_ITEM_EFFECT:
                return new LoseItemEffect(quantity);
            case GAIN_GAMEPOINT_EFFECT:
                return new GainGamePointEffect(quantity);
            default:
                break;
        }
        throw new IllegalArgumentException();
    }

}

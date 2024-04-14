package model.config.cardeffects;

public enum CardEffectType {
    TELEPORTATION_EFFECT, // Téléporte sur 'cellID'
    MONEY_EFFECT,   // Gagne 'amount'
    LOSE_PROPERTY_EFFECT, // Perd une propriété choisie aléatoirement
    SWAP_POSITION_EFFECT, // Swap de position avec un autre joueur choisi aléatoirement.
    GAIN_ITEM_EFFECT, // Gagne l'item 'item'.
    LOSE_ITEM_EFFECT, // Perd un item choisi aléatoirement.
    GAIN_GAMEPOINT_EFFECT; // Gagne un point.
}

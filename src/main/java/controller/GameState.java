package controller;

public enum GameState {
    GAME_START,
    // Couche 0 : Le tour du joueur commence (Vue).
    NEW_TURN,
    // Couche 1 : Le joueur est en prison (Vue) ou joue son dé (Vue + Contrôleur).
    PLAY_DICE,
    IN_JAIL,
    // Couche 2 : Le joueur est sur une case propriété ou shop ou intersection (Vue + Contrôleur).
    ASK_INTERSECTION,
    ASK_PROPERTY,
    ASK_SHOP,
    // Couche 3 : Le joueur fini son tour (Vue).
    END_TURN,
    GAME_END;
}

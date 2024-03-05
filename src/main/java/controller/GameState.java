package controller;

public enum GameState {
    // Couche 0 : La partie commence (Vue) {Affiche le titre du plateau/jeu}.
    GAME_START,
    // Couche 1 : Le tour du joueur commence (Vue) {Affiche le nom du joueur et son nombre de tour actuel + restants}.
    NEW_TURN,
    // Couche 2 : Le joueur est en prison (Vue) {Affiche un truc/animation pour montrer qu'il est en prison}.
    IN_JAIL,
    // Couche 3 : Le joueur joue son dé (Vue + Contrôleur) {Affiche un dé (ou des dés) que le joueur peut cliquer}.
    PLAY_DICE,
    // Couche 4 : Le joueur tombe sur une case normale (Vue) ou propriété ou shop ou intersection (Vue + Contrôleur)
    // {Affiche une fenêtre correspondant à la case et auquel le joueur peut intéragir avec}.
    NORMAL_CELL,
    ASK_INTERSECTION,
    ASK_PROPERTY,
    ASK_SHOP,
    // Couche 4 : Le joueur fini son tour (Vue) {Affiche une animation ou transition vers le prochain joueur}.
    END_TURN,
    // Couche 5 : La partie est terminée (Vue) {Affiche un menu avec les stats (?) A revoir}
    GAME_END;
}

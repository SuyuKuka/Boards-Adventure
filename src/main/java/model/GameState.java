package model;

public enum GameState {
    // -1 : attente d'une décision du joueur
    // > 0 temps que met le jeu pour faire l'animation
    // Couche 0 : La partie commence (Vue) {Affiche le titre du plateau/jeu}.
    GAME_START(2),
    // Couche 1 : Le tour du joueur commence (Vue) {Affiche le nom du joueur et son nombre de tour actuel + restants}.
    NEW_TURN(1),
    // Couche 2 : Le joueur est en prison (Vue) {Affiche un truc/animation pour montrer qu'il est en prison}.
    IN_JAIL(0.5),
    // Couche 3 : Le joueur joue son dé (Vue + Contrôleur) {Affiche un dé (ou des dés) que le joueur peut cliquer}.
    ROLL_DICE(-1),
    PLAYER_MOVE(0),
    // Couche 4 : Le joueur tombe sur une case normale (Vue) ou propriété ou shop ou intersection (Vue + Contrôleur)
    // {Affiche une fenêtre correspondant à la case et auquel le joueur peut intéragir avec}.
    ASK_INTERSECTION(-1),
    ASK_PROPERTY(-1),
    ASK_SHOP(-1),
    NORMAL_CELL(0.5),
    // Couche 4 : Le joueur fini son tour (Vue) {Affiche une animation ou transition vers le prochain joueur}.
    END_TURN(0.5),
    // Couche 5 : La partie est terminée (Vue) {Affiche un menu avec les stats (?) A revoir}
    GAME_END(2);

    private final double animationTime;

    GameState(final double animationTime) {
        this.animationTime = animationTime;
    }

    public double getAnimationTime() {
        return animationTime;
    }

    public boolean isWaitingPlayerInteraction() {
        return animationTime == -1;
    }
}

package model;

/**
 * Classe qui contient toutes les "étapes" du jeu ainsi que son temps d'animation associé.
 */
public enum GameState {
    // -1 : attente d'une décision du joueur
    // >= 0 temps que met le jeu pour faire l'animation

    GAME_START(2),

    NEW_TURN(1),

    IN_JAIL(0.5),
    ROLL_DICE(-1),

    PLAYER_MOVE(0),

    ASK_INTERSECTION(-1),
    PLAYER_ARRIVED(0),

    START(0),
    JAIL(0),
    VOID(0),
    GAIN(-1),
    LOSS(-1),
    TELEPORTATION(0),
    CARD(0),
    KINGDOM(0),
    DANGER(0),

    ASK_PROPERTY(-1),
    ASK_SHOP(-1),

    END_TURN(0.5),

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

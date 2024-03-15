package controller;

import game.Game;

/**
 * Classe abstraite étendue par {@link MenuController} et {@link GameController} qui contient la méthode {@link #run()}.
 */
public abstract sealed class Controller permits MenuController, GameController {

    private final Game game;

    public Controller(final Game game) {
        this.game = game;
    }

    public abstract void run();

    public final void updateNanoUpTime(final long nanoTime) {
        game.setNanoUpTime(nanoTime - game.getNanoStartTime());
    }

    public final long getNanoStartTime() {
        return game.getNanoStartTime();
    }

    public final Game getGame() {
        return game;
    }

    public final double getUpTime() {
        return game.getUpTime();
    }

    public final long getNanoUpTime() {
        return game.getNanoUpTime();
    }

}

package controller;

import game.Game;
import view.CorePanel;

public abstract sealed class Controller permits MenuController, GameController {

    public static final int NANO_S = (int) 10E9;

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

    public final double getUpTime() {
        return game.getUpTime();
    }

    public final long getNanoUpTime() {
        return game.getNanoUpTime();
    }

    public static double getNanoS() {
        return (double) NANO_S;
    }

    public abstract void setCorePanel(CorePanel corePanel);

}

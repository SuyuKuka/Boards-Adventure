package controller;

import game.Game;
import view.View;
import view.menu.MenuPanel;

public final class MenuController extends Controller {

    private final MenuPanel menuPanel;

    public MenuController(final Game game, final View view) {
        super(game);
        menuPanel = view.initNewMenuPanel(this);
    }

    @Override
    public void run() {
        while (true) {
            updateNanoUpTime(System.nanoTime());
        }
    }

}

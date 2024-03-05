package controller;

import game.Game;
import view.CorePanel;
import view.menu.MenuPanel;

public final class MenuController extends Controller {

    private MenuPanel menuPanel;

    public MenuController(final Game game) {
        super(game);
    }

    @Override
    public void run() {
        while (true) {
            updateNanoUpTime(System.nanoTime());
        }
    }

    @Override
    public void setCorePanel(final CorePanel corePanel) {
        this.menuPanel = (MenuPanel) corePanel;
    }

}

package controller;

import java.util.HashMap;
import java.util.ArrayList;

import game.Game;
import view.View;
import view.game.GameMapGraphics;
import view.menu.MenuPanel;

/**
 * Classe qui fait office de controlleur pour le Menu et appelle {@link #run()}. <p>
 * Elle contient seulement un {@link MenuPanel} qui s'occupe de la vue.
 */
public final class MenuController extends Controller {

    private final MenuPanel menuPanel;

    public MenuController(final Game game, final View view) {
        super(game);
        menuPanel = view.initNewMenuPanel(this);
    }

    public void startGame(final GameMapGraphics gameMapGraphics, final HashMap<String, Integer> gameConfig,
        final ArrayList<HashMap<String, String>> playersConfig) {
            getGame().startGame(gameMapGraphics, gameConfig, playersConfig);
    }

    @Override
    public void run() {
        while (true) {
            updateNanoUpTime(System.nanoTime());
        }
    }

}

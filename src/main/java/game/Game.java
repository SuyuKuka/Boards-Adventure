package game;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

import controller.Controller;
import controller.resources.Resources;
import model.Model;
import model.config.GameMap;
import model.config.Team;
import model.config.players.HumanPlayer;
import model.config.players.Player;
import view.View;
import view.game.GameMapGraphics;

/**
 * Classe mère de l'application.
 * <p> Crée un objet {@link view.View} au lancement du programme et initialise {@link controller.resources.Resources}
 * et l'application en fonction de {@link #mode}.
 *
 * <p> L'application est scindé en 2 parties : le menu et le jeu en lui-même.
 *
 * <p> Lorsque le joueur est dans le menu :
 * un {@link view.menu.MenuPanel} est crée et {@link view.View} agit de manière indépendante.
 *
 * <p> Lorsque le joueur est dans le jeu :
 * un {@link view.game.GamePanel}, un {@link controller.Controller} et un {@link model.Model} sont créés
 * et le {@link model.Model}, {@link controller.Controller} et la {@link view.View} agissent ensembles.
 *
 * @see controller.Controller
 * @see model.Model
 * @see view.View
 * @see controller.resources.Resources
 */
public final class Game {

    public static final int NANO_S = (int) 10E8;
    public static final Random RAND = new Random();

    private static final String DEFAULT_GAME_MAP_NAME = "map4";

    private Controller controller = null;
    private Model model = null;
    private final View view;

    private Mode mode = Mode.GAME;
    /** Si vraie, alors change de mode.*/
    private volatile boolean switchMode = false;

    private Game() {
        Resources.loadCommonResources();
        view = new View(this);
        init();
        run();
    }

    /**
     * Méthode qui initialise l'application en fonction de {@link #mode}.
     */
    private void init() {
        if (view.hasPanel()) {
            view.removePanel();
        }
        switch (mode) {
            case MENU:
                Resources.loadMenuResources();
                view.initNewMenuPanel();
                break;
            case GAME:
                GameMapGraphics gameMapGraphics = Resources.loadGameResources(DEFAULT_GAME_MAP_NAME);
                if (gameMapGraphics == null) {
                    System.out.println("Map non reconnu.\nVoir DEFAULT_GAME_MAP_NAME dans Game.java et "
                    + "les maps disponibles dans le dossier resources/maps");
                    System.exit(0);
                }
                if (model == null) {
                    model = createTestModel(gameMapGraphics);
                }
                controller = new Controller(this, model, view);
                break;
            default:
                break;
        }
    }

    /**
     * Méthode qui update le jeu.
     */
    private void run() {
        while (true) {
            if (switchMode) {
                switch (mode) {
                    case MENU:
                        mode = Mode.GAME;
                        break;
                    default:
                        mode = Mode.MENU;
                        break;
                }
                init();
                switchMode = false;
            } else if (controller != null) {
                controller.run();
            }
        }
    }

    private Model createTestModel(final GameMapGraphics gameMapGraphics) {
        final HashMap<String, Integer> gameSettings = new HashMap<>();
        gameSettings.put("turns", 45);
        final ArrayList<Player> players = new ArrayList<>();
        players.add(new HumanPlayer(1, "human1", new Team(1, "team1")));
        // players.add(new ComputerPlayer(2, "comp1", new Team(2, "team2")));
        // players.add(new HumanPlayer(3, "human2", new Team(3, "team3")));
        // players.add(new ComputerPlayer(4, "comp2", new Team(4, "team4")));
        System.out.println("MAP PAR DEFAUT GENEREE");
        return new Model(gameMapGraphics.getGameMap(), gameSettings, players);
    }

    private ArrayList<Player> createPlayers(final ArrayList<HashMap<String, String>> playersConfig) {
        final ArrayList<Player> players = new ArrayList<>();
        players.add(new HumanPlayer(1, "human1", new Team(1, "team1")));
        return players;
    }

    public void createModel(final String mapName, final HashMap<String, Integer> gameConfig,
        final ArrayList<HashMap<String, String>> playersConfig) {
        Resources.loadGameMapGraphics(mapName);
        model = new Model(Resources.getGameMapGraphics().getGameMap(), gameConfig, createPlayers(playersConfig));
    }

    public void setSwitchMode(final boolean switchMode) {
        this.switchMode = switchMode;
    }

    public boolean getSwitchMode() {
        return switchMode;
    }

    public Mode getMode() {
        return mode;
    }

    public static void main(final String[] args) {
        Game launcher = new Game();
    }
}

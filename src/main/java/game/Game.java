package game;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

import controller.Controller;
import controller.resources.Resources;
import model.Model;
import model.config.Team;
import model.config.players.HumanPlayer;
import model.config.players.Player;
import view.View;

/**
 * Classe mère du jeu qui contient un controlleur {@link Controller} ou {@link MenuController}
 * et appelle la méthode {@link Controller#run()}.
 * Le jeu est scindé en 2 parties : le menu et le jeu en lui-même.
 * C'est le controlleur associé qui s'occupe de sa partie.
 */
public final class Game {

    public static final int NANO_S = (int) 10E8;
    public static final Random RAND = new Random();

    private static final Mode DEFAULT_MODE = Mode.MENU;
    private static final String DEFAULT_GAME_MAP_NAME = "map4";

    private final long nanoStartTime = System.nanoTime();
    private long nanoUpTime = 0;

    private Model model;
    private View view;
    private Controller controller;

    private Mode mode;
    /** Si vraie, alors change de mode.*/
    private volatile boolean switchMode;

    private Game() {
        this.mode = DEFAULT_MODE;
        controller = null;
        model = null;
        switchMode = false;
        view = new View(this);
        init();
        run();
    }

    private void init() {
        if (view.hasPanel()) {
            view.removePanel();
            Resources.clearModeResources();
        }
        switch (mode) {
            case MENU:
                Resources.loadMenuResources();
                view.initNewMenuPanel();
                break;
            case GAME:
                Resources.loadGameResources(DEFAULT_GAME_MAP_NAME);
                if (Resources.getGameMapGraphics() == null) {
                    System.out.println("Map non reconnu.");
                    System.out.println("voir DEFAULT_GAME_MAP_NAME dans Game.java et les maps disponibles dans resources/maps");
                    System.exit(0);
                }
                if (model == null) {
                    model = createTestModel();
                    System.out.println("MAP PAR DEFAUT GENEREE");
                }
                controller = new Controller(this, model, view);
                break;
            default:
                break;
        }
    }

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

    private Model createTestModel() {
        final HashMap<String, Integer> gameSettings = new HashMap<>();
        final ArrayList<Player> players = new ArrayList<>();
        players.add(new HumanPlayer(1, "human1", new Team(1, "team1")));
        // players.add(new ComputerPlayer(2, "comp1", new Team(2, "team2")));
        // players.add(new HumanPlayer(3, "human2", new Team(3, "team3")));
        // players.add(new ComputerPlayer(4, "comp2", new Team(4, "team4")));
        return new Model(Resources.getGameMapGraphics().getGameMap(), gameSettings, players);
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

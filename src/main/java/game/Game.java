package game;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

import controller.Controller;
import controller.GameController;
import controller.MenuController;
import controller.resources.Resources;
import model.Model;
import model.config.GameMap;
import model.config.Team;
import model.config.players.HumanPlayer;
import model.config.players.Player;
import view.View;
import view.game.GameMapGraphics;

/**
 * Classe mère du jeu qui contient un controlleur {@link GameController} ou {@link MenuController}
 * et appelle la méthode {@link Controller#run()}.
 * Le jeu est scindé en 2 parties : le menu et le jeu en lui-même.
 * C'est le controlleur associé qui s'occupe de sa partie.
 */
public final class Game {

    public static final int NANO_S = (int) 10E8;
    public static final Random RAND = new Random();

    public enum Mode {
        MENU, GAME;
    }

    private long nanoStartTime = System.nanoTime();
    private long nanoUpTime = 0;

    private View view;
    private Controller controller;

    private Mode mode;
    private static final String DEFAULT_GAME_MAP_NAME = "map4";

    private Game(final Mode mode) {
        this.mode = mode;
        view = new View();
        init();
        controller.run();
    }

    private void init() {
        switch (mode) {
            case MENU:
                controller = new MenuController(this, view);
                break;
            case GAME:
                final GameMapGraphics gameMapGraphics = Resources.loadGameMapsGraphics(DEFAULT_GAME_MAP_NAME);
                if (gameMapGraphics == null) {
                    System.out.println("map non reconnu");
                    System.exit(0);
                }
                controller = new GameController(this, view, gameMapGraphics,
                    createTestModel(gameMapGraphics.getGameMap()));
                break;
            default:
                break;
        }
    }

    private Model createTestModel(final GameMap gameMap) {
        final HashMap<String, Integer> gameSettings = new HashMap<>();
        gameSettings.put("turns", 35);
        final ArrayList<Player> players = new ArrayList<>();
        players.add(new HumanPlayer(1, "human1", new Team(1, "team1")));
        // players.add(new ComputerPlayer(2, "comp1", new Team(2, "team2")));
        // players.add(new HumanPlayer(3, "human2", new Team(3, "team3")));
        // players.add(new ComputerPlayer(4, "comp2", new Team(4, "team4")));
        return new Model(gameMap, gameSettings, players);
    }

    public void startGame(final GameMapGraphics gameMapGraphics, final HashMap<String, Integer> gameConfig,
        final ArrayList<HashMap<String, String>> playersConfig) {
            final ArrayList<Player> players = createPlayers(playersConfig);
        controller = new GameController(this, view, gameMapGraphics, createTestModel(gameMapGraphics.getGameMap()));
    }

    private ArrayList<Player> createPlayers(final ArrayList<HashMap<String, String>> playersConfig) {
        final ArrayList<Player> players = new ArrayList<>();
        // TODO créer players à partir des playersConfig.
        return players;
    }

    public void goToMenu() {
        // MENU
    }

    public long getNanoStartTime() {
        return nanoStartTime;
    }

    public double getUpTime() {
        return nanoUpTime / NANO_S;
    }

    public long getNanoUpTime() {
        return nanoUpTime;
    }

    public void setNanoUpTime(final long nanoUpTime) {
        this.nanoUpTime = nanoUpTime;
    }

    public static void main(final String[] args) {
        final Mode mode = Mode.MENU;
        switch (mode) {
            case MENU:
                Resources.loadResources(mode);
                break;
            case GAME:
                Resources.loadResources(mode, DEFAULT_GAME_MAP_NAME);
                break;
            default:
                break;
        }
        Game launcher = new Game(mode);
    }
}

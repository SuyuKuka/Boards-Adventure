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
import model.config.players.ComputerPlayer;
import model.config.players.HumanPlayer;
import model.config.players.Player;
import view.View;

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

    private Mode mode = Mode.GAME;
    private static String testModelMapName = "map4";

    private Game() {
        init();
        controller.run();
    }

    private void init() {
        view = new View();
        switch (mode) {
            case MENU:
                controller = new MenuController(this, view);
                break;
            case GAME:
                controller = new GameController(this, view, Resources.getGameMapGraphics(testModelMapName));
                break;
            default:
                break;
        }
    }

    public Model createTestModel(final GameMap gameMap) {
        final HashMap<String, Integer> gameSettings = new HashMap<>();
        gameSettings.put("turns", 35);
        final ArrayList<Player> players = new ArrayList<>();
        players.add(new HumanPlayer(1, "human1", new Team(1, "team1")));
        players.add(new ComputerPlayer(2, "comp1", new Team(2, "team2")));
        players.add(new HumanPlayer(3, "human2", new Team(3, "team3")));
        players.add(new ComputerPlayer(4, "comp2", new Team(4, "team4")));
        return new Model(gameMap, gameSettings, players);
    }

    public void startGame() {
        // start game
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
        Resources.loadResources();
        Game launcher = new Game();
    }
}

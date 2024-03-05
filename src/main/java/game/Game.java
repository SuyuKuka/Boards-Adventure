package game;

import java.util.HashMap;
import java.util.ArrayList;

import controller.Controller;
import controller.GameController;
import controller.MenuController;
import controller.Resources;
import model.Model;
import model.config.GameMap;
import model.config.Team;
import model.config.players.ComputerPlayer;
import model.config.players.HumanPlayer;
import model.config.players.Player;
import view.GameMapGraphics;
import view.View;
import view.game.GamePanel;
import view.menu.MenuPanel;

public final class Game {

    public static final int NANO_S = (int) 10E9;

    public enum Mode {
        MENU, GAME;
    }

    private long nanoStartTime = System.nanoTime();
    private long nanoUpTime = 0;

    private Controller controller;

    private Mode mode = Mode.GAME;
    private static String testMapName = "map4";

    private Game() {
        init();
        controller.run();
    }

    private void init() {
        View view = new View();
        switch (mode) {
            case MENU:
                controller = new MenuController(this);
                view.init(new MenuPanel(view, (MenuController) controller));
                break;
            case GAME:
                GameMapGraphics gameMapGraphics = Resources.getGameMapGraphics(testMapName);
                controller = new GameController(this, createTestModel(gameMapGraphics.getGameMap()));
                view.init(new GamePanel(view, (GameController) controller, gameMapGraphics));
                break;
            default:
                break;
            }
        controller.setCorePanel(view.getCorePanel());
    }

    private Model createTestModel(final GameMap gameMap) {
        final HashMap<String, Integer> gameSettings = new HashMap<>();
        gameSettings.put("turns", 35);
        final ArrayList<Player> players = new ArrayList<>();
        players.add(new HumanPlayer(1, "human1", new Team(1, "team1")));
        players.add(new ComputerPlayer(2, "comp1", new Team(2, "team2")));
        players.add(new HumanPlayer(3, "human2", new Team(3, "team3")));
        players.add(new ComputerPlayer(3, "comp2", new Team(4, "team4")));
        return new Model(gameMap, gameSettings, players);
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

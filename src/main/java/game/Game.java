package game;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingUtilities;

import controller.Controller;
import controller.resources.Resources;
import model.Model;
import model.config.GameMap;
import model.config.Team;
import model.config.players.HumanPlayer;
import view.View;

/**
 * Classe mère du jeu.
 * Le jeu est scindé en 2 parties : le menu et le jeu en lui-même.
 * Le menu ne contient qu'un fichier {@link MenuPanel} qui s'occupe de la vue dans le menu.
 * Lorsqu'une partie est lancée, un {@link Model} et un {@link Controller} est crée.
 * La vue est modifiée pour remplacer le {@link MenuPanel} par un {@link GamePanel}.
 */
public final class Game {

    public static final int NANO_S = (int) 10E8;
    public static final Random RAND = new Random();

    private Mode mode = Mode.GAME;

    private View view;
    private Model model = null;
    private Controller controller = null;

    private Game() {
        SwingUtilities.invokeLater(() -> {
            view = new View(this);
            Resources.loadCommonResources();
            initGame();
        });
    }

    public void initGame() {
        if (view.hasPanel()) {
            view.removePanel();
        }
        switch (mode) {
            case MENU:
                Resources.loadMenuResources();
                view.initNewMenuPanel();
                break;
            case GAME:
                if (model == null) {
                    model = createTestModel("map4");
                }
                Resources.loadGameResources(model.getGameMap().getMapName());
                controller = new Controller(this, model, view);
                controller.gameUpdate();
                break;
            default:
                break;
        }
    }

    /**
     * Crée un {@link Model} test si l'application a été lancée avec {@link #mode} = {@link Mode#GAME}.
     * @param mapName
     * @return {@link Model}.
     */
    private Model createTestModel(final String mapName) {
        final GameMap gameMap = Resources.loadGameMap(mapName);
        final HashMap<String, Integer> gameSettings = new HashMap<>();
        gameSettings.put("turns", 45);
        final ArrayList<Team> teams = new ArrayList<>();
        final Team team1 = new Team("team1");
        team1.addPlayer(new HumanPlayer("human1", "class1", team1, null));
        teams.add(team1);
        return new Model(gameMap, gameSettings, teams);
    }

    /**
     * Crée un {@link Model} à partir du nom de la map, les paramètres du jeu et les paramètres des joueurs.
     * @param mapName
     * @param gameSettings
     * @param playersConfig
     */
    public void createModel(final String mapName, final HashMap<String, Integer> gameSettings,
        final ArrayList<HashMap<String, String>> playersConfig) {
        model = new Model(Resources.loadGameMap(mapName), gameSettings, createTeams(playersConfig));
    }

    private ArrayList<Team> createTeams(final ArrayList<HashMap<String, String>> playersConfig) {
        final ArrayList<Team> teams = new ArrayList<>();
        final Team team1 = new Team("team1");
        team1.addPlayer(new HumanPlayer("human1", "class1", team1, null));
        return teams;
    }

    public void setMode(final Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public static void main(final String[] args) {
        Game launcher = new Game();
    }
}

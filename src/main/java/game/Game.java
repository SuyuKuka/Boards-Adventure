package game;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingUtilities;

import org.json.JSONArray;

import controller.Controller;
import controller.resources.Resources;
import model.Model;
import model.config.GameMap;
import model.config.cardseffect.CardEffect;
import model.config.cells.Cell;
import model.config.players.ComputerPlayer;
import model.config.players.HumanPlayer;
import model.config.players.Player;
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

    private static final boolean TEST_MODE = true;
    private Mode mode = Mode.MENU;

    private View view;
    private Model model = null;
    private Controller controller = null;

    private Game() {
        SwingUtilities.invokeLater(() -> {
            view = new View(this);
            Resources.loadCommonResources();
            if (mode == Mode.GAME) {
                startTempGame();
            } else {
                startMenu();
            }
        });
    }

    public void startMenu() {
        if (view.hasPanel()) {
            view.removePanel();
        }
        mode = Mode.MENU;
        Resources.loadMenuResources();
        view.initNewMenuPanel();
    }

    /**
     * Méthode qui lance le jeu avec les paramètres de la map et des joueurs donnés.
     * @param mapConfig
     * @param playersConfig
     */
    public void startGame(final HashMap<String, String> mapConfig,
            final ArrayList<HashMap<String, String>> playersConfig) {
        if (view.hasPanel()) {
            view.removePanel();
        }
        mode = Mode.GAME;
        final String mapName = mapConfig.get("mapName");
        final HashMap<Integer, Cell> cells = new HashMap<>();
        final ArrayList<CardEffect> cardEffects = new ArrayList<>();
        final HashMap<String, HashMap<String, String>> startItems = new HashMap<>();
        final HashMap<String, HashMap<String, String>> shopItems = new HashMap<>();
        final JSONArray jCells = Resources.loadGameResources(mapName, cells, cardEffects, startItems, shopItems);

        final GameMap gameMap = new GameMap(mapName, cardEffects, cells, startItems, shopItems);
        final ArrayList<Player> players = createPlayers(playersConfig, gameMap);
        model = new Model(gameMap, players, mapConfig);
        controller = new Controller(this, model);
        controller.setGamePanel(view.initNewGamePanel(controller, jCells));
        controller.gameUpdate();
    }

    /**
     * Méthode qui lance le jeu avec des paramètres par défaut.
     * Cette méthode ne doit jamais être call lors du produit final !!!
     */
    public void startTempGame() {
        final HashMap<String, String> mapConfig = new HashMap<>();
        final ArrayList<HashMap<String, String>> playersConfig = new ArrayList<>();
        initTempGameConfig(mapConfig, playersConfig);
        startGame(mapConfig, playersConfig);
    }

    private void initTempGameConfig(final HashMap<String, String> gameConfig,
            final ArrayList<HashMap<String, String>> playersConfig) {
        // gameconfig
        gameConfig.put("mapName", "map4");
        gameConfig.put("totalTurns", "45");
        // playersconfig
        final HashMap<String, String> player1 = new HashMap<>();
        player1.put("type", "human");
        player1.put("name", "human1");
        player1.put("className", "boardClass");
        player1.put("teamColor", "#0b03fc");
        player1.put("itemName", "HOE");
        playersConfig.add(player1);
    }

    private ArrayList<Player> createPlayers(final ArrayList<HashMap<String, String>> playersConfig,
            final GameMap gameMap) {
        final ArrayList<Player> playersList = new ArrayList<>();
        playersConfig.forEach(playerConfig -> {
            final String name = playerConfig.get("name");
            final boolean isHuman = playerConfig.get("type") == "human";
            final String className = playerConfig.get("className");
            final String teamColor = playerConfig.get("teamColor");
            final String itemName = playerConfig.get("itemName");
            final int startPos = gameMap.getStartCell().getId();
            if (isHuman) {
                playersList.add(new HumanPlayer(name, className, teamColor,
                    gameMap.generateItem(itemName, false), startPos));
            } else {
                playersList.add(new ComputerPlayer(name, className, teamColor,
                    gameMap.generateItem(itemName, false), startPos));
            }
        });
        return playersList;
    }

    public Mode getMode() {
        return mode;
    }

    public static void main(final String[] args) {
        Game launcher = new Game();
    }
}

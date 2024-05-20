package game;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingUtilities;

import org.json.JSONObject;

import controller.Controller;
import controller.resources.Audio;
import controller.resources.Resources;
import model.Model;
import model.Player;

import model.config.GameMap;
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
    private static final String[] FFA_COLORS = {"#d7191c", "#fdae61", "#abdda4", "#2b83ba"};
    private static final String START_TEMP_MAP = "kingdom"; // kingdom, spaceship, villanova, frostharbor

    private Mode mode = Mode.MENU;

    private View view;
    private Model model = null;
    private Controller controller = null;

    private Game() {
        SwingUtilities.invokeLater(() -> {
            Resources.loadCommonResources();
            view = new View(this);
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
        Audio.stopMusic();
        Audio.playMusic("MenuTheme.wav");
        mode = Mode.MENU;
        Resources.loadMenuResources();
        view.initNewMenuPanel();
    }

    /**
     * Méthode qui lance le jeu avec les paramètres de la map et des joueurs donnés.
     * @param gameConfig
     * @param playersConfig
     */
    public void startGame(final HashMap<String, String> gameConfig,
            final ArrayList<HashMap<String, String>> playersConfig) {
        if (view.hasPanel()) {
            view.removePanel();
        }
        Audio.stopMusic();
        Audio.playMusic("GameTheme.wav");
        mode = Mode.GAME;
        final String mapName = gameConfig.get("mapName");
        final JSONObject mapConfig = Resources.loadGameResources(mapName);

        final GameMap gameMap = new GameMap(mapName, mapConfig);
        final ArrayList<Player> players = createPlayers(playersConfig, gameMap,
            gameConfig.get("mode").equals("FFA"));
        model = new Model(gameMap, players, gameConfig, mapConfig);
        controller = new Controller(this, model);
        controller.setGamePanel(view.initNewGamePanel(controller, mapConfig));
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
        gameConfig.put("mapName", START_TEMP_MAP);
        gameConfig.put("totalTurns", "45");
        gameConfig.put("mode", "FFA");
        gameConfig.put("doubleDice", "false");
        // playersconfig
        // player1
        final HashMap<String, String> player1 = new HashMap<>();
        player1.put("type", "human");
        player1.put("name", "Joueur 1");
        player1.put("className", "Board");
        player1.put("teamColor", "#7ebbed");
        player1.put("itemName", "FARMER_HOE");
        playersConfig.add(player1);
        // computer1
        final HashMap<String, String> computer1 = new HashMap<>();
        computer1.put("type", "computer");
        computer1.put("name", "Ordinateur 1");
        computer1.put("className", "Gloria");
        computer1.put("teamColor", "#7ebbed");
        computer1.put("itemName", "FARMER_HOE");
        playersConfig.add(computer1);
        // player2
        final HashMap<String, String> player2 = new HashMap<>();
        player2.put("type", "human");
        player2.put("name", "Joueur 2");
        player2.put("className", "Bacchus");
        player2.put("teamColor", "#d16264");
        player2.put("itemName", "SACRED_BOOK");
        playersConfig.add(player2);
        // computer2
        final HashMap<String, String> computer2 = new HashMap<>();
        computer2.put("type", "computer");
        computer2.put("name", "Ordinateur 2");
        computer2.put("className", "May");
        computer2.put("teamColor", "#d16264");
        computer2.put("itemName", "MAGIC_WAND");
        playersConfig.add(computer2);
    }

    private ArrayList<Player> createPlayers(final ArrayList<HashMap<String, String>> playersConfig,
            final GameMap gameMap, final boolean isFFA) {
        final ArrayList<Player> playersList = new ArrayList<>();
        for (int index = 0; index < playersConfig.size(); index++) {
            final HashMap<String, String> playerConfig = playersConfig.get(index);
            final String name = playerConfig.get("name");
            final boolean isHuman = playerConfig.get("type") == "human";
            final String className = playerConfig.get("className");
            String teamColor = "";
            if (isFFA) {
                teamColor = FFA_COLORS[index];
            } else if (teamColor.equals("red")) {
                teamColor = "#d16264";
            } else {
                teamColor = "#7ebbed";
            }
            final String itemName = playerConfig.get("itemName").toUpperCase();
            final int startPos = gameMap.getStartCell().getId();
            playersList.add(new Player(name, isHuman, className, teamColor,
                gameMap.generateItem(itemName, false), startPos));
        }
        return playersList;
    }

    public Mode getMode() {
        return mode;
    }

    public static void main(final String[] args) {
        new Game();
    }

}

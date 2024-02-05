package game;
import controller.MonopolyController;
import model.MonopolyModel;
import view.MonopolyView;

/**
 * Classe mère qui s'occupe du jeu. Le jeu fonctionne avec la structure MVC.
 * Les images du jeu se mettent à jour par rapport à {@link #MAX_FPS}.
 * La logique derrière le jeu se met à jour par rapport à {@link #MAX_UPS}.
 */
public class Monopoly implements Runnable {

    public enum Mode {
        MENU, GAME;
    }

    private final static int NANO_S = (int) 10E8;
    /** Changez la valeur pour modifier le nombre de FPS voulu/max */
    private final static int MAX_FPS = 60;
    /** Changez la valeur pour modifier le nombre de UPS voulu/max */
    private final static int MAX_UPS = 60;

    private double runningTime = 0;

    private Mode mode;

    private MonopolyController controller = new MonopolyController();
    private MonopolyModel model = new MonopolyModel();
    private MonopolyView view = new MonopolyView();

    public Monopoly() {
        controller.setModel(model);
        controller.setView(view);
        view.setController(controller);
        Resources.loadResources();
        view.init();
        runWithInfo();
    }


    /**
     * Méthode qui met à jour la logique du jeu et son affichage indépendamment de chacun en fonction des valeurs données par défaut.
     */
    @Override
    public void run() {
        final double frameTime = NANO_S / MAX_FPS;
        final double updateTime = NANO_S / MAX_UPS;

        double updateDeltaT = 0;
        double frameDeltaT = 0;
        
        long startTime = System.nanoTime();
        while (true) {
            long currentTime = System.nanoTime();
            updateDeltaT += currentTime - startTime;
            frameDeltaT += currentTime - startTime;
            runningTime += (currentTime - startTime) / (double) NANO_S;
            startTime = currentTime;

            if (mode == Mode.GAME && updateDeltaT >= updateTime) {
                model.update(updateDeltaT / NANO_S);
                updateDeltaT -= updateTime;
            }
            
            if (frameDeltaT >= frameTime) {
                view.render();
                frameDeltaT -= frameTime;
            }

            System.out.println(runningTime);
        }
    }

    /**
     * Méthode qui met à jour la logique du jeu et son affichage indépendamment de chacun en fonction des valeurs données par défaut.
     * Elle affiche également quelques informations sur le jeu dans le terminal.
     */
    public void runWithInfo() {
        final double frameTime = NANO_S / MAX_FPS;
        final double updateTime = NANO_S / MAX_UPS;

        int updates = 0;
        double updateDeltaT = 0;
        int frames = 0;
        double frameDeltaT = 0;
        
        long startTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        while (true) {
            long currentTime = System.nanoTime();
            updateDeltaT += currentTime - startTime;
            frameDeltaT += currentTime - startTime;
            runningTime += (currentTime - startTime) / (double) NANO_S;
            startTime = currentTime;

            if (mode == Mode.GAME && updateDeltaT >= updateTime) {
                model.update(updateDeltaT / NANO_S);
                // System.out.println("LAST GAME UPDATE : " + updateDeltaT / NANO_S + " SEC");
                updates++;
                updateDeltaT -= updateTime;
            }
            if (frameDeltaT >= frameTime) {
                view.render();
                // System.out.println("LAST RENDER UPDATE : " + frameDeltaT / NANO_S + " SEC");
                frames++;
                frameDeltaT -= frameTime;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("UPS : " + updates + " | FPS : " + frames);
                updates = 0;
                frames = 0;
                timer += 1000;
            }
        }
    }

    public Mode getMode() {
        return mode;
    }

    public static void main(String[] args) {
        Monopoly monopoly = new Monopoly();
    }
    
}

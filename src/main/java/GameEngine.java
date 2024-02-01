/**
 * Classe mère qui s'occupe des updates du jeu.
 */
public class GameEngine implements Runnable {

    private final static int NANO_S = (int) 10E8;
    /** Changez la valeur pour modifier le nombre de FPS voulu/max */
    private final static int MAX_FPS = 60;
    /** Changez la valeur pour modifier le nombre de UPS voulu/max */
    private final static int MAX_UPS = 60;

    private boolean running = false;

    public GameEngine() {
        running = true;
        String input = System.console().readLine("Entrez \"oui\" pour avoir les infos sur la console, autre chose sinon : ");
        if (input.equals("oui") || input.equals("\"oui\"")) {
            runWithInfo();
        } else {
            run();
        }
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
        while (running) {
            long currentTime = System.nanoTime();
            updateDeltaT += currentTime - startTime;
            frameDeltaT += currentTime - startTime;
            startTime = currentTime;

            if (updateDeltaT >= frameTime) {
                update();
                updateDeltaT -= frameTime;
            }
            if (frameDeltaT >= updateTime) {
                render();
                frameDeltaT -= updateTime;
            }
        }
    }

    /**
     * Méthode qui met à jour la logique du jeu et son affichage indépendamment de chacun en fonction des valeurs données par défaut.
     * Elle affiche également un message chaque seconde dans le terminal indiquant le nombre de UPS et FPS qui s'est passé la dernière seconde.
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
        while (running) {
            long currentTime = System.nanoTime();
            updateDeltaT += currentTime - startTime;
            frameDeltaT += currentTime - startTime;
            startTime = currentTime;

            if (updateDeltaT >= frameTime) {
                update();
                updates++;
                updateDeltaT -= frameTime;
            }
            if (frameDeltaT >= updateTime) {
                render();
                frames++;
                frameDeltaT -= updateTime;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("UPS : " + updates + " | FPS : " + frames);
                updates = 0;
                frames = 0;
                timer += 1000;
            }
        }
    }

    public void update() {
        // System.out.println("update");
    }

    public void render() {
        // System.out.println("render");
    }
    
}

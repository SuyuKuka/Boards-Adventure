package model.config.role;

import model.config.Player;

public abstract class Role {

    /** Lancé de dés nécessaires avant que le pouvoir se réactive. */
    private final int powerCD;
    /** == 0 si actif, != 0 si désactivé. */
    private int currentCD = 0;
    private boolean powerUsed = false;

    public Role(final int powerCD) {
        this.powerCD = powerCD;
    }

    /**
     * Met à jour les tours restant du cooldown du pouvoir.
     */
    public void updateCD() {
        if (powerUsed || currentCD >= 1) {
            currentCD = currentCD + 1 % powerCD;
            if (powerUsed) {
                powerUsed = false;
            }
        }
    }
    
    /**
     * Renvoie vraie si le pouvoir peut être utilisé.
     * Les conditions dépendent du rôle.
     * @return
     */
    abstract boolean canUsePower();

    /**
     * Utilise le pouvoir.
     */
    abstract void usePower(final Player player);

    public int getPowerCD() {
        return powerCD;
    }

    public int getCurrentCD() {
        return currentCD;
    }

    public void setCurrentCD(final int currentCD) {
        this.currentCD = currentCD;
    }
}

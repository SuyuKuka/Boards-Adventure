package model.config.role;

import model.config.players.Player;

/**
 * Classe abstraite qui est étendu par tous les rôles que peut prendre un {@link Player}.
 */
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

    abstract boolean canUsePower();

    abstract void usePower(Player player);

    public final int getPowerCD() {
        return powerCD;
    }

    public final int getCurrentCD() {
        return currentCD;
    }

    public final void setCurrentCD(final int currentCD) {
        this.currentCD = currentCD;
    }
}

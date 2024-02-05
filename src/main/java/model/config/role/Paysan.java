package model.config.role;

import model.config.Joueur;

public class Paysan extends Role {

    private static final int POWER_CD = 2;

    public Paysan() {
        super(POWER_CD);
    }

    /**
     * Condition du {@link Paysan} : Cooldown < {@link #POWER_CD}. 
     */
    @Override
    public boolean canUsePower() {
        return getCurrentCD() == 0;
    }
    /**
     * Pouvoir du {@link Paysan} : Passe son tour mais gagne X argent.
     */
    @Override
    void usePower(final Joueur joueur) {
        throw new UnsupportedOperationException("Unimplemented method 'usePower'");
    }
    
}

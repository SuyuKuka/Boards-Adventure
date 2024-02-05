package model.config.role;

import model.config.Player;

public class Farmer extends Role {
    
    private static final int POWER_CD = 2;

    public Farmer() {
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
    void usePower(final Player player) {
        throw new UnsupportedOperationException("Unimplemented method 'usePower'");
    }
    
}

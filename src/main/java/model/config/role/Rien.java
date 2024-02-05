package model.config.role;

import model.config.Joueur;

public class Rien extends Role {

    private static final int POWER_CD = 999;

    public Rien() {
        super(POWER_CD);
    }

    @Override
    boolean canUsePower() {
        return false;
    }

    @Override
    void usePower(Joueur joueur) {
        return;
    }
    
}

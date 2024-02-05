package model.config.role;

import model.config.Player;

public class Nothing extends Role {

    private static final int POWER_CD = 999;

    public Nothing() {
        super(POWER_CD);
    }

    @Override
    boolean canUsePower() {
        return false;
    }

    @Override
    void usePower(Player player) {
        return;
    }
    
}

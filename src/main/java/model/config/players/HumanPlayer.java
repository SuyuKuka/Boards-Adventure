package model.config.players;

import model.config.items.Item;

public class HumanPlayer extends Player {

    public HumanPlayer(final String username, final String className, final String teamColor,
            final Item startItem, final int startPos) {
        super(username, className, teamColor, startItem, startPos);
    }

}

package model.config.players;

import model.config.Team;
import model.config.items.Item;

public class HumanPlayer extends Player {

    public HumanPlayer(final String username, final Team team, final Item startItem) {
        super(username, team, startItem);
    }

}

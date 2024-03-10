package model.config.players;

import model.config.Item;
import model.config.Team;

public class HumanPlayer extends Player {

    public HumanPlayer(final int playerID, final String username, final Team team) {
        super(playerID, username, team, new Item("test"));
    }

}

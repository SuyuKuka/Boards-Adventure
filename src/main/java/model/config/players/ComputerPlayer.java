package model.config.players;

import model.config.items.Item;

public class ComputerPlayer extends Player {

    public ComputerPlayer(final String username, final String className, final String teamColor,
            final Item startItem, final int startPos) {
        super(username, className, teamColor, startItem, startPos);
    }

}

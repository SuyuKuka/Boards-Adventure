package model.config.cells;

import java.util.ArrayList;
import java.util.Random;

import model.GameState;
import model.Model;
import model.config.cells.cards.CardManagement;
import model.config.cells.cards.MoneyEffect;
import model.config.players.Player;

public class Card extends Cell {

    private ArrayList<CardManagement> cards = new ArrayList<>();

    public Card(int id, CellType cellType, int nextCellID) {
        super(id, cellType, nextCellID);
        if (cards.size() == 0) {
            createAllCards();
        }
    }

    private void createAllCards() {
        CardManagement gainMoneyCard1 = new CardManagement(new MoneyEffect(100));
        CardManagement gainMoneyCard2 = new CardManagement(new MoneyEffect(200));
        CardManagement gainMoneyCard3 = new CardManagement(new MoneyEffect(300));
        CardManagement gainMoneyCard4 = new CardManagement(new MoneyEffect(400));
        CardManagement gainMoneyCard5 = new CardManagement(new MoneyEffect(500));

        cards.add(gainMoneyCard1);
        cards.add(gainMoneyCard2);
        cards.add(gainMoneyCard3);
        cards.add(gainMoneyCard4);
        cards.add(gainMoneyCard5);
    }

    /**
     * Selectionne aléatoirement une carte dans cards et l'utilise.
     *
     * @param  player  the player to apply the effect to
     * @param  model   the game model
     */
    @Override
    public void playCellEffect(Player player, Model model) {
        Random rand = new Random();
        int i = rand.nextInt(cards.size());
        cards.get(i).applyEffect(player, model);
        model.setGameState(GameState.CARD);
    }

    public final ArrayList<CardManagement> getCards() {
        return cards;
    }
}

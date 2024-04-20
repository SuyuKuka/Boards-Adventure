package model.config.items;

import model.Model;
import model.config.players.Player;

public abstract class ItemEffect {

    public abstract void applyEffect(Player player, Model model);
    // ? = pas sur
    // Objet de départ :
    // Gagner de l'argent mais ne lance pas son dé (img : houe)
    // Petit dé, le petit dé possède que 4 chiffres {0,1,2,3}, utilisable tous les x tours
    // (img : dé avec les petits chiffres)

    // Objet du shop :
    // Emmene sur une case kingdom ou point de départ (point de départ mieux que kingdom) (img : etoile?)
    // Dé triple
    // Dé double
    // Acheter des cards? (img : carte)

}

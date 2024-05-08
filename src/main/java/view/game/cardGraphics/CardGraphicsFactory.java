package view.game.cardGraphics;

import view.game.DataPanel;
import javax.swing.JPanel;

public final class CardGraphicsFactory {

    public static CardGraphics createEmptyCardGraphics(final DataPanel dataPanel, final JPanel parent) {
        return new EmptyCardGraphics(dataPanel, parent);
    }
    public static CardGraphics createCardGraphics() {
        return null;
    }
}

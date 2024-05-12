package view.game.cardGraphics;

import view.game.GamePanel;


public final class CardGraphicsFactory {

    public static CardGraphics createEmptyCardGraphics(final GamePanel gamePanel, final int width, final int height) {
        return new EmptyCardGraphics(gamePanel, width, height);
    }
    public static CardGraphics createCardGraphics() {
        return null;
    }
}

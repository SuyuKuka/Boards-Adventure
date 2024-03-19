package view.game;

import javax.swing.JLabel;

public abstract class EntityGraphics extends JLabel {
    private final int defaultWidth;
    private final int defaultHeight;

    public EntityGraphics(final int defaultWidth, final int defaultHeight) {
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
    }

    public abstract void update(double zoomX, double zoomY);

}

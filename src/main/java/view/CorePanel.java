package view;

import javax.swing.JLayeredPane;

public abstract class CorePanel extends JLayeredPane {
    private final View frame;

    public CorePanel(final View frame) {
        this.frame = frame;
        setBounds(0, 0, frame.getWidth(), frame.getHeight());
    }

    public abstract void resized();

    public final View getFrame() {
        return frame;
    }
}

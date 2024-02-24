package model.config;

import java.util.ArrayList;
import java.util.Arrays;

public class Dice {
    private ArrayList<Integer> values = new ArrayList<>();

    /**
     * Dé par défaut à 6 faces.
     */
    public Dice() {
        values.addAll(Arrays.asList(1, 2, 3, 4, 5, 6));
    }

    /**
     * Dé contenant toutes les valeurs entières entre les 2 valeurs données.
     * @param min
     * @param max
     */
    public Dice(final int min, final int max) {
        for (int i = min; i <= max; i++) {
            values.add(i);
        }
    }

    public Dice(final ArrayList<Integer> values) {
        this.values = values;
    }

    public final ArrayList<Integer> getValues() {
        return values;
    }
}

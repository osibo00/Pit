package productions.darthplagueis.pit.view.util;

import java.util.Random;

/**
 * Utility class used to create random X and Y positions within the bounds
 * of PitView. Created as a singleton so that the random object does not need
 * to be recreated whenever the randomInt() method is used.
 */
public class PitViewHelper {

    private Random random;

    private static final PitViewHelper INSTANCE = new PitViewHelper();

    private PitViewHelper() {
        random = new Random();
    }

    public static PitViewHelper getINSTANCE() {
        return INSTANCE;
    }

    /**
     * Accepts min and max integer values to produce a random integer that is
     * within the bounds of the min and max values.
     *
     * @param min
     * @param max
     * @return int
     */
    public int randomInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }
}

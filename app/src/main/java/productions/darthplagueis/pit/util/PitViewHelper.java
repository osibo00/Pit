package productions.darthplagueis.pit.util;

import java.util.Random;

public class PitViewHelper {

    private Random random;

    private static final PitViewHelper INSTANCE = new PitViewHelper();

    private PitViewHelper() {
        random = new Random();
    }

    public static PitViewHelper getINSTANCE() {
        return INSTANCE;
    }

    public float randomFloat(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }
}

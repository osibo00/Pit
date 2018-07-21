package productions.darthplagueis.pit.util;

import java.util.Comparator;
import java.util.Random;

import productions.darthplagueis.pit.view.PitPoint;

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

    public static Comparator<PitPoint> compareByXPos = new Comparator<PitPoint>() {
        @Override
        public int compare(PitPoint o1, PitPoint o2) {
            int o1XPosition = (int) o1.getxPosition();
            int o2XPosition = (int) o2.getxPosition();
            return Integer.compare(o1XPosition, o2XPosition);
        }
    };
}

package productions.darthplagueis.pit.util;

import java.util.Comparator;

import productions.darthplagueis.pit.view.PitPoint;

public class PitPointComparator implements Comparator<PitPoint> {

    @Override
    public int compare(PitPoint o1, PitPoint o2) {
        int o1Position = (int) o1.getXPosition();
        int o2Position = (int) o2.getXPosition();
        return Integer.compare(o1Position, o2Position);
    }
}

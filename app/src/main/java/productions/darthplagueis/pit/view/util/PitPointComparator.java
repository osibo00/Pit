package productions.darthplagueis.pit.view.util;

import java.util.Comparator;

import productions.darthplagueis.pit.view.PitPoint;

/**
 * Utility class that is used in PitView to sort its PitPoint arrayList
 * by the point's X positions.
 */
public class PitPointComparator implements Comparator<PitPoint> {

    /**
     * Compares two int values and returns -1, 0 or 1 that the comparator uses
     * to sort the X positions from least to greatest.
     *
     * @param o1
     * @param o2
     * @return int
     */
    @Override
    public int compare(PitPoint o1, PitPoint o2) {
        return Integer.compare(o1.getXPosition(), o2.getXPosition());
    }
}

package controller.comparators;

import Ex2.Band;

public class OriginComparator extends BandComparator {
    @Override
    public int compare(Band o1, Band o2) {
        return o1.getOrigin().compareToIgnoreCase(o2.getOrigin());
    }
}

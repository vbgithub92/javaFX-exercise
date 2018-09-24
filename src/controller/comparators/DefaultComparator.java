package controller.comparators;

import Ex2.Band;

public class DefaultComparator extends BandComparator {
    @Override
    public int compare(Band o1, Band o2) {
        return o1.getSerialNumber() - o2.getSerialNumber();
    }
}

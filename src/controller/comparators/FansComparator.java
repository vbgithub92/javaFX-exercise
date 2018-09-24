package controller.comparators;

import Ex2.Band;

public class FansComparator extends BandComparator {
    @Override
    public int compare(Band o1, Band o2) {
        return o1.getNumOfFans() - o2.getNumOfFans();
    }
}

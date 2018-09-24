package controller.comparators;

import Ex2.Band;

public class NameComparator extends BandComparator {

        @Override
        public int compare(Band o1, Band o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }


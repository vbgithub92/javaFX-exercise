package datalayer;

import datastractures.BandsArrayList;
import datastractures.BandsHashMap;
import Ex2.Band;

import java.io.IOException;

public interface BandDataAccess {
    final static int NUMBER_OF_OBJECTS = 1;
    BandsArrayList readAllBands() throws IOException, ClassNotFoundException;

    BandsHashMap getBandsMappedByName() throws IOException, ClassNotFoundException;

    void saveBands(Band[] bands) throws IOException;
}

package datalayer;

import datastractures.BandsArrayList;
import datastractures.BandsHashMap;
import Ex2.Band;

import java.io.*;
import java.util.ListIterator;

public class BandDataAccessObject implements BandDataAccess {
    private static int number_of_objects = 0;
    private final String BANDS_FILE = ".\\bands.bin";
    private final String BANDS_FILE_OUT = ".\\bandsOut.bin";
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private BandDataAccessObject() throws IOException {

    }

    public static BandDataAccessObject getInstance() throws IOException {
        if (number_of_objects > NUMBER_OF_OBJECTS)
        return null;
    else
    { number_of_objects++;
        return new BandDataAccessObject();
    }
    }
    public static void reduceNumberOfObjects()
    { number_of_objects--;
    }
    public static int getNumberOfObjects()
    { return number_of_objects;
    }
    public static void resetNumberOfObjects()
    {  number_of_objects = 0;
    }

    @Override
    public BandsArrayList readAllBands() throws IOException, ClassNotFoundException {
        ois = new ObjectInputStream(new FileInputStream(BANDS_FILE));
        Object ob = ois.readObject();
        ois.close();
        return new BandsArrayList((Band[]) ob);
    }

    @Override
    public BandsHashMap getBandsMappedByName() throws IOException, ClassNotFoundException {

        BandsHashMap bhm = new BandsHashMap();
        BandsArrayList bands = readAllBands();
        ListIterator<Band> itr = bands.listIterator();
        while(itr.hasNext()){
            Band temp = itr.next();
            bhm.put(temp.getName(),temp);
        }
        return bhm;
    }

    @Override
    public void saveBands(Band[] bands) throws IOException {
        oos = new ObjectOutputStream(new FileOutputStream(BANDS_FILE_OUT));
        oos.writeObject(bands);
        oos.close();
    }
}

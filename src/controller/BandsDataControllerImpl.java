package controller;

import Ex2.Band;
import controller.comparators.BandComparator;
import controller.comparators.DefaultComparator;
import datalayer.BandDataAccessObject;
import datastractures.BandsArrayList;
import datastractures.BandsHashMap;

import java.io.IOException;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Stack;

public class BandsDataControllerImpl implements BandsDataController {

    private BandDataAccessObject dataAccess;
    private BandsArrayList bandArr;
    private BandsHashMap bandMap;
    private ListIterator<Band> itr;

    private Stack<BandsDataCommand> commands = new Stack<>();
    private Band tempBend;

    {
        try {
            dataAccess = BandDataAccessObject.getInstance();
            bandArr = dataAccess.readAllBands();
            bandMap = dataAccess.getBandsMappedByName();
            itr = bandArr.listIterator();
            tempBend = bandArr.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void goToStart(){
        tempBend = bandArr.get(0);
        itr = bandArr.listIterator();
    }
    
    @Override
    public Band previous() {
        BandsDataCommand prev = new Previous();
        prev.execute();
        commands.push(prev);
        return tempBend;
    }

    @Override
    public Band next() {
        BandsDataCommand next = new Next();
        next.execute();
        commands.push(next);
        return tempBend;
    }

    @Override
    public void sort(Comparator<Band> comparator) {
        BandsDataCommand sort = new Sort(comparator);
        sort.execute();
        commands.push(sort);
    }

    @Override
    public void add(Band band) {
        BandsDataCommand add = new Add(band);
        add.execute();
        commands.push(add);
    }

    @Override
    public void remove() {
        if (tempBend != null) {
            BandsDataCommand remove = new Remove(tempBend);
            remove.execute();
            commands.push(remove);
        }
    }

    @Override
    public void undo() {
        if (!commands.isEmpty())
            commands.pop().undo();
    }

    @Override
    public void revert() {
        while (!commands.isEmpty())
            commands.pop().undo();
        goToStart();
    }

    @Override
    public void save() throws IOException {
        dataAccess.saveBands(bandArr.toArray());
    }

    @Override
    public Band getBandByName(String name) {
        return bandMap.get(name);
    }

    public Band getCurrentBand(){
        if(tempBend == null)
            next();
         return tempBend;
    }


    //Commands
    class Next implements BandsDataCommand {

        @Override
        public void execute() {
            if (itr.hasNext())
                tempBend = itr.next();
            else {
                goToStart();
            }
        }

        @Override
        public void undo() {
            if (itr.hasPrevious())
                tempBend = itr.previous();
        }

    }

    class Previous implements BandsDataCommand {

        @Override
        public void execute() {
            if (itr.hasPrevious())
                tempBend = itr.previous();
            else {
                tempBend = bandArr.get(bandArr.size() - 1);
                itr = bandArr.listIterator(bandArr.size() - 1);
            }
        }

        @Override
        public void undo() {
            if (itr.hasNext())
                tempBend = itr.next();
        }
    }

    class Add implements BandsDataCommand {
        private Band band1;

        public Add(Band band) {
            band1 = band;
        }

        @Override
        public void execute() {
            bandArr.add(band1);
            bandMap.put(band1.getName(), band1);
        }

        @Override
        public void undo() {
            bandArr.remove(band1);
            bandMap.remove(band1.getName());
        }
    }

    class Remove implements BandsDataCommand {
        private Band bandAux;
        private String bandAuxName;
        private int index;

        public Remove(Band band) {
            index = bandArr.indexOf(band);
            bandAux = band;
            bandAuxName = band.getName();
        }

        @Override
        public void execute() {
            bandArr.remove(bandAux);
            bandMap.remove(bandAuxName);
        }

        @Override
        public void undo() {
            bandArr.add(index, bandAux);
            bandMap.put(bandAux.getName(), bandAux);
        }
    }

    class Sort implements BandsDataCommand {
        private Comparator<Band> comparator1;
        private DefaultComparator dc = new DefaultComparator();

        public Sort(Comparator<Band> comparator){
            comparator1 = comparator;
        }
        @Override
        public void execute() {
            if(comparator1 instanceof BandComparator)
                bandArr.sort(comparator1);
        }

        @Override
        public void undo() {
            bandArr.sort(dc);
        }
    }
}

package datastractures;

import Ex2.Band;

import java.io.Serializable;
import java.util.*;

public class BandsArrayList implements List<Band>, RandomAccess, Cloneable, Serializable {

    private int size = 0;
    private int capacity = 1;
    private Band[] data = new Band[capacity];

    public BandsArrayList(Band[] bands)
    { for (int i = 0; i < bands.length; i++)
        add(bands[i]);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++)
            if (data[i].equals(o))
                return true;
        return false;
    }

    @Override
    public Band[] toArray() {
        return Arrays.copyOf(data, size);
    }

    @Override
    // not to implement
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(Band band) {
        if(size < capacity){
            data[size] = band;
            size++;
        }else{
            Band[] temp = Arrays.copyOf(data, size + 1);
            temp[size] = band;
            size++;
            capacity = size * 2;
            data = Arrays.copyOf(temp, capacity);
        }

        return this.contains(band);
    }

    @Override
    public boolean remove(Object o) {
        for(int i = 0; i < size; i++)
            if(data[i].equals(o)) {
               remove(i);
            }
            return !(this.contains(o));
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element: c) {
          if(!this.contains(element))
              return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Band> c) {
        for (Object element: c) {
            this.add((Band)element);
        }
        return containsAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Band> c) {
        int i = index;
        for (Object element: c) {
            this.add(i,(Band)element);
            i++;
        }
        return containsAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object element: c) {
            this.remove(element);
        }
        for (Object element: c) {
            if(this.contains(element))
                return false;
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        this.clear();
        this.addAll((Collection<? extends Band>) c);
        return containsAll(c);
    }

    @Override
    public void clear() {
        size = 0;
        capacity = 1;
        data = new Band[capacity];
    }

    private void checkIndex(int index)
    { if (index < 0 || index >= size)
        throw new IndexOutOfBoundsException
                ("Index: " + index + ", Size: " + size);
    }

    @Override
    public Band get(int index) {
        checkIndex(index);
        return data[index];
    }

    @Override
    public Band set(int index, Band element) {
        checkIndex(index);
        Band temp = data[index];
        data[index] = element;
        return temp;
    }

    @Override
    public void add(int index, Band element) {
        checkIndex(index);
        Band[] temp = new Band[size + 1];
        // copy all the elements up to the index
        System.arraycopy(data, 0, temp, 0, index);
        // add the new element at the requested index
        temp[index] = element;
        // add the rest of the elements
        System.arraycopy(data, index, temp, index + 1, size - index);
        size++;
        data = Arrays.copyOf(temp,size);
    }

    @Override
    public Band remove(int index) {
        checkIndex(index);
        Band temp = data[index];
        for (int j = index; j < size - 1; j++)
            data[j] = data[j + 1];
        data[size - 1] = null; // last element
        size--;
        return temp;
    }

    @Override
    public int indexOf(Object o) {
        // returns -1 if not found
        for(int i = 0; i<size; i++)
            if(data[i].equals(o))
                return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        // returns -1 if not found
        int index = -1;
        for(int i = 0; i<size; i++)
            if(data[i].equals(o))
                index = i;
        return index;
    }

    @Override
    public List<Band> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public Iterator<Band> iterator() {
        return null;
    }

    @Override
    public ListIterator<Band> listIterator() {
        return new BandListIterator();
    }

    @Override
    public ListIterator<Band> listIterator(int index) {
        {
            return new BandListIterator(index);
        }
    }

    public class BandListIterator implements ListIterator<Band> {
        private int current = 0;
        private int last = -1;

        public BandListIterator(){}

        public BandListIterator(int current)
        { this.current = current;
        }
        @Override
        public boolean hasNext()
        { return (current < size);
        }
        @Override
        public Band next()
        { if (!hasNext()) throw new NoSuchElementException();
            Band temp = data[current];
            last = current;
            current++;
            return temp;
        }
        @Override
        public void remove()
        { if (last == -1) throw new IllegalStateException();
            BandsArrayList.this.remove(last);
            current = last;
            last = -1;
        }
        @Override
        public void add(Band b)
        { if (current < 0 || current > data.length)
            throw new IndexOutOfBoundsException();
            BandsArrayList.this.add(current, b);
            current++;
            last = -1;
        }
        @Override
        public boolean hasPrevious()
        { if (current <= 0) return false;
        else return true;
        }
        @Override
        public int nextIndex()
        { return current;
        }
        @Override
        public Band previous()
        { if (!hasPrevious()) throw new NoSuchElementException();
            current--;
            last = current;
            return BandsArrayList.this.get(current);
        }
        @Override
        public int previousIndex()
        { return current -1;
        }
        @Override
        public void set(Band b)
        { if (last == -1) throw new IllegalStateException();
            BandsArrayList.this.set(last, b);
        }

    }
} // BandsArrayList

package com.zenyte.utils;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class IntLinkedList {

    private final IntList list;
    private int counter;

    public IntLinkedList() {
        this.list = new IntArrayList(100);
    }

    public void enqueue(int element) {
        list.add(element);
    }

    public int peek() {
        if (counter >= list.size()) {
            throw new IllegalStateException("Counter is larger than size.");
        }
        return list.getInt(counter);
    }

    public int getLast() {
        return nthPeek(size() - 1);
    }

    public int remove() {
        if (counter >= list.size()) {
            throw new IllegalStateException("Counter is larger than size.");
        }
        int value = list.getInt(counter++);
        if (isEmpty())
            clear();
        return value;
    }

    public int nthPeek(int numToSkip) {
        int index = counter + numToSkip;
        if (index >= list.size()) return 0;
        return list.getInt(index);
    }

    public int size() {
        return list.size() - counter;
    }

    public boolean isEmpty() {
        int size = list.size();
        return size == 0 || size - counter == 0;
    }

    public void clear() {
        counter = 0;
        list.clear();
    }

    public void resetAllButFirst() {
        final int first = peek();
        clear();
        enqueue(first);
    }


}